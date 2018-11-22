package com.movilizer.mds.connector.utils;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.model.MovilizerRequestSink;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class MovilizerCoreUtilsImpl implements MovilizerCoreUtils {
    private static Log logger = LogFactory.getLog(MovilizerCoreUtilsImpl.class);

    private final MovilizerConnectorConfig config;
    private final MovilizerMetricService metrics;

    @Autowired
    public MovilizerCoreUtilsImpl(MovilizerConnectorConfig config, MovilizerMetricService metrics) {
        this.config = config;
        this.metrics = metrics;
    }

    @Override
    public MovilizerRequest createUploadRequest() {
        MovilizerRequest request = new MovilizerRequest();
        request.setRequestTrackingKey(String.valueOf(System.currentTimeMillis()));
        return request;
    }

    @Override
    public Flux<MovilizerResponse> responsesSource(String sourceName) {
        return responsesSource(sourceName, null);
    }

    @Override
    public Flux<MovilizerResponse> responsesSource(String sourceName, String responseQueue) {
        MovilizerDistributionService mds = config.createMdsInstance();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Created new Movilizer response source %s", sourceName));
        }
        AtomicReference<String> requestAcknowledgeKey = new AtomicReference<>();
        requestAcknowledgeKey.set("");
        return Flux.interval(config.getPollingInterval())
                .doOnSubscribe(subscription -> {
                    metrics.sourceSubscribersIncrement(sourceName);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("New subscriber %s to %s source",
                                subscription.toString(), sourceName));
                    }
                })
                .map(time -> {
                    MovilizerRequest request = mds.prepareDownloadRequest(config.getSystemId(), config.getPassword(),
                            config.getPushConsolidatedElementsSize(), new MovilizerRequest());
                    request.setResponseQueue(responseQueue);
                    request.setRequestAcknowledgeKey(requestAcknowledgeKey.get());
                    request.setUseAutoAcknowledge(false);
                    return request;
                })
                .flatMap(request ->
                        Mono.fromFuture(mds.getReplyFromCloud(request))
                                .doOnError(throwable -> metrics.sourceRequestErrorSubmit(sourceName))
                                .elapsed()
                                .map(tuple -> {
                                    metrics.sourceResponseTimeSubmit(sourceName, tuple.getT1());
                                    return tuple.getT2();
                                })
                )
                .doOnNext(response -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("New response from %s source", sourceName));
                    }
                    requestAcknowledgeKey.set(response.getRequestAcknowledgeKey());
                    if (mds.responseHasErrors(response)) {
                        metrics.sourceWebserviceErrorSubmit(sourceName,
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
                .doAfterTerminate(() -> {
                    // Do a final acknowledge to avoid double processing of replies
                    MovilizerRequest request = mds.prepareUploadRequest(config.getSystemId(), config.getPassword(),
                            new MovilizerRequest());
                    request.setUseAutoAcknowledge(true);
                    request.setRequestAcknowledgeKey(requestAcknowledgeKey.get());
                    Mono.fromFuture(mds.getReplyFromCloud(request))
                            .subscribe(response -> {
                                if (logger.isDebugEnabled()) {
                                    logger.debug(String.format("Closing request from %s source sent and acknowledged",
                                            sourceName));
                                }
                            });
                    metrics.sourceSubscribersReset(sourceName);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Terminated Movilizer response source %s",
                                sourceName));
                    }
                });
    }

    @Override
    public MovilizerRequestSink createRequestSink(String name) {
        return MovilizerRequestSink.create(config, name, metrics);
    }

    @Override
    public MovilizerRequestSink createRequestSink(String name, MovilizerRequestSink.Strategy strategy) {
        return MovilizerRequestSink.create(config, name, metrics, strategy);
    }

    @Override
    public MovilizerRequestSink createRequestSink(String name, String responseQueue) {
        return MovilizerRequestSink.create(config, name, responseQueue, metrics);
    }

    @Override
    public MovilizerRequestSink createRequestSink(String name, String responseQueue,
                                                  MovilizerRequestSink.Strategy strategy) {
        return MovilizerRequestSink.create(config, name, responseQueue, metrics, strategy);
    }
}
