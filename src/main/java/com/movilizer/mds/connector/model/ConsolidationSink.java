package com.movilizer.mds.connector.model;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.model.consolidation.RequestConsolidationUtil;
import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;

import java.util.List;

class ConsolidationSink extends MovilizerRequestSink {

    ConsolidationSink(MovilizerConnectorConfig config, String name,
                      MovilizerMetricService metrics) {
        super(config, name, metrics);
        upstream = TopicProcessor.share(name, config.getPushConsolidatedElementsSize());
        downstream = upstream
                .doOnNext(request -> {
                    if (queueSize != null) {
                        queueSize.inc();
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("New request from %s sink", this.name));
                    }
                    if (logger.isTraceEnabled()) {
                        logger.trace(mds.requestToString(request));
                    }
                })
                .bufferTimeout(config.getPushConsolidatedElementsSize(),
                        config.getPushingInterval())
                .doOnNext(requests -> {
                    if (queueSize != null) {
                        queueSize.dec(requests.size());
                    }
                })
                .flatMap((List<MovilizerRequest> requests) ->
                        Mono.justOrEmpty(RequestConsolidationUtil.consolidateRequests(requests, metrics))
                )
                .doOnNext(request -> {
                    if (request != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("Consolidated request from %s sink", this.name));
                        }
                        if (logger.isTraceEnabled()) {
                            logger.trace(mds.requestToString(request));
                        }
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("Consolidated request was empty from %s sink", this.name));
                        }
                    }

                })
                .map(this::overrideRequestConfig)
                .map(request -> {
                    if (request == null){
                        return new MovilizerResponse();

                    } else {
                        return mds.getReplyFromCloudSync(request);
                    }
                })
                .doOnNext(response -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("New response from %s sink", this.name));
                    }
                    if (mds.responseHasErrors(response)) {
                        metrics.sinkWebserviceErrorSubmit(name,
                                response.getDocumentError().size() +
                                        response.getMasterdataError().size() +
                                        response.getMoveletError().size() +
                                        response.getMoveletAssignmentError().size() +
                                        response.getParticipantInstallError().size() +
                                        response.getMoveletAssignmentDeleteError().size());
                        if (logger.isErrorEnabled()) {
                            logger.error(mds.responseErrorsToString(response));
                        }
                    }
                    if (logger.isTraceEnabled()) {
                        logger.trace(mds.responseToString(response));
                    }
                })
                .doOnSubscribe(subscription -> {
                    metrics.sinkSubscribersIncrement(name);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("New subscriber %s to %s sink",
                                subscription.toString(), this.name));
                    }
                })
                .doAfterTerminate(() -> {
                    metrics.sinkSubscribersReset(name);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Terminated Movilizer request sink %s",
                                this.name));
                    }
                });
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Created new Movilizer request sink %s", toString()));
        }
    }
}
