package com.movilizer.mds.connector.model;

import com.codahale.metrics.Counter;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;

import java.util.List;

public class MovilizerRequestSink {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerRequestSink.class);


    private static final String CONNECTOR_UPLOAD_QUEUE_SUFFIX = "-connector-upload-";

    private final MovilizerConnectorConfig config;
    private final String name;
    private final String ownerThreadId;
    private final MovilizerMetricService metrics;
    private TopicProcessor<MovilizerRequest> upstream;
    private Flux<MovilizerResponse> downstream;
    private MovilizerDistributionService mds;

    private MovilizerRequestSink(MovilizerConnectorConfig config, String name,
                                 MovilizerMetricService metrics) {
        this.config = config;
        this.name = name;
        this.ownerThreadId = String.valueOf(Thread.currentThread().getId());
        this.metrics = metrics;
        mds = config.createMdsInstance();
        final Counter queueSize = metrics.createSinkQueueSizeCounter(name);
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
                .map(this::consolidateRequests)
                .doOnNext(request -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Consolidated request from %s sink", this.name));
                    }
                    if (logger.isTraceEnabled()) {
                        logger.trace(mds.requestToString(request));
                    }
                })
                .map(this::overrideRequestConfig)
                .flatMap(request ->
                        Mono.fromFuture(mds.getReplyFromCloud(request))
                                .doOnError(throwable -> metrics.sinkRequestErrorSubmit(name))
                                .elapsed()
                                .map(tuple -> {
                                    metrics.sinkResponseTimeSubmit(name, tuple.getT1());
                                    return tuple.getT2();
                                })
                )
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

    protected MovilizerRequest consolidateRequests(List<MovilizerRequest> requests) {
        return requests.get(0);
    }

    protected MovilizerRequest overrideRequestConfig(MovilizerRequest request) {
        request.setSystemId(config.getSystemId());
        request.setSystemPassword(config.getPassword());
        request.setSynchronousResponse(true);
        request.setUseAutoAcknowledge(true);
        request.setNumResponses(0);
        request.setResponseQueue(getResponseQueue());
        return request;
    }

    public Flux<MovilizerResponse> sendRequest(MovilizerRequest request) {
        upstream.onNext(request);
        return downstream;
    }

    public Flux<MovilizerResponse> responses() {
        return downstream;
    }

    private boolean isMetricsSet() {
        return this.metrics != null;
    }

    private String getResponseQueue() {
        return name + CONNECTOR_UPLOAD_QUEUE_SUFFIX + ownerThreadId;
    }

    @Override
    public String toString() {
        return "MovilizerRequestSink{" +
                "name=" + name +
                ", ownerThread='" + ownerThreadId + '\'' +
                ", requestBuffer='" + config.getPushConsolidatedElementsSize().toString() + '\'' +
                ", pushingInterval='" + config.getPushingInterval().toString() + '\'' +
                ", endpoint='" + config.getEndpoint() + '\'' +
                ", systemId='" + config.getSystemId() + '\'' +
                ", responseQueue='" + getResponseQueue() + '\'' +
                '}';
    }

    public static MovilizerRequestSink create(MovilizerConnectorConfig config, String name,
                                              MovilizerMetricService metrics) {
        return new MovilizerRequestSink(config, name, metrics);
    }
}
