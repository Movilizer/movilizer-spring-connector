package com.movilizer.mds.connector.model;

import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.TopicProcessor;

class PassThroughSink extends MovilizerRequestSinkBase {
    private static final Logger logger = LoggerFactory.getLogger(PassThroughSink.class);

    PassThroughSink(MovilizerConnectorConfig config, String name,
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
                .map(this::overrideRequestConfig)
                //TODO: throttle and wrap blocking call
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
                    if (queueSize != null) {
                        queueSize.dec(1);
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
