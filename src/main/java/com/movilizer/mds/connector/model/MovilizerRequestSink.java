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
import reactor.core.publisher.TopicProcessor;

public abstract class MovilizerRequestSink {
    public enum Strategy {PASS_THROUGH, CONSOLIDATE}
    static final Logger logger = LoggerFactory.getLogger(MovilizerRequestSink.class);

    private static final String CONNECTOR_UPLOAD_QUEUE_SUFFIX = "-connector-upload-";

    private final MovilizerConnectorConfig config;
    final String name;
    private final String ownerThreadId;
    private final MovilizerMetricService metrics;
    final Counter queueSize;
    protected final MovilizerDistributionService mds;
    TopicProcessor<MovilizerRequest> upstream;
    Flux<MovilizerResponse> downstream;


    MovilizerRequestSink(MovilizerConnectorConfig config, String name,
                         MovilizerMetricService metrics) {
        this.config = config;
        this.name = name;
        this.ownerThreadId = String.valueOf(Thread.currentThread().getId());
        this.metrics = metrics;
        mds = config.createMdsInstance();
        queueSize = metrics.createSinkQueueSizeCounter(name);
    }

    MovilizerRequest overrideRequestConfig(MovilizerRequest request) {
        if (request == null) return null;
        request.setSystemId(config.getSystemId());
        request.setSystemPassword(config.getPassword());
        request.setSynchronousResponse(true);
        request.setUseAutoAcknowledge(true);
        request.setNumResponses(0);
        request.setResponseQueue(getResponseQueue());
        request.setRequestAcknowledgeKey(request.getRequestAcknowledgeKey());
        request.setRequestTrackingKey(request.getRequestTrackingKey());
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
        return create(config, name, metrics, Strategy.PASS_THROUGH);
    }

    public static MovilizerRequestSink create(MovilizerConnectorConfig config, String name,
                                              MovilizerMetricService metrics, Strategy strategy) {
        switch (strategy) {
            case CONSOLIDATE:
                return new ConsolidationSink(config, name, metrics);
            case PASS_THROUGH:
            default:
                return new PassThroughSink(config, name, metrics);
        }
    }
}
