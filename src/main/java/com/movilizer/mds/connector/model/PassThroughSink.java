package com.movilizer.mds.connector.model;

import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;
import reactor.core.scheduler.Schedulers;

class PassThroughSink extends MovilizerRequestSinkBase {
    private static final Logger logger = LoggerFactory.getLogger(PassThroughSink.class);

    PassThroughSink(MovilizerConnectorConfig config, String name, Boolean isSynchronous,
                    MovilizerMetricService metrics) {
        super(config, name, isSynchronous, metrics);
        setup(config, isSynchronous, metrics);
    }

    PassThroughSink(MovilizerConnectorConfig config, String name, String responseQueue, Boolean isSynchronous,
                    MovilizerMetricService metrics) {
        super(config, name, responseQueue, isSynchronous, metrics);
        setup(config, isSynchronous, metrics);
    }

    private void setup(MovilizerConnectorConfig config, Boolean isSynchronous, MovilizerMetricService metrics) {
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
                .flatMap(request -> {
                    if (request == null){
                        return Mono.just(new MovilizerResponse());
                    } else {
                        Mono<MovilizerResponse> response;
                        if (isSynchronous) {
                            response = Mono.fromCallable(() -> mds.getReplyFromCloudSync(request));
                            response.subscribeOn(Schedulers.elastic());
                        } else {
                            response = Mono.fromFuture(mds.getReplyFromCloud(request));
                        }
                        return response;

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
