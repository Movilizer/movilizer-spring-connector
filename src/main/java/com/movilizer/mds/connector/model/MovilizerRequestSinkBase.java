package com.movilizer.mds.connector.model;

import com.codahale.metrics.Counter;
import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

public abstract class MovilizerRequestSinkBase implements MovilizerRequestSink {

    private static final String CONNECTOR_UPLOAD_QUEUE_SUFFIX = "-connector-upload-";

    private final MovilizerConnectorConfig config;
    final String name;
    private final String ownerThreadId;
    private final MovilizerMetricService metrics;
    final Counter queueSize;
    protected final MovilizerDistributionService mds;
    TopicProcessor<MovilizerRequest> upstream;
    Flux<MovilizerResponse> downstream;


    MovilizerRequestSinkBase(MovilizerConnectorConfig config, String name,
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

    @Override
    public Flux<MovilizerResponse> sendRequest(MovilizerRequest request) {
        upstream.onNext(request);
        return downstream;
    }

    @Override
    public Flux<MovilizerResponse> responses() {
        return downstream;
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

    private boolean isMetricsSet() {
        return this.metrics != null;
    }

    private String getResponseQueue() {
        return name + CONNECTOR_UPLOAD_QUEUE_SUFFIX + ownerThreadId;
    }
}
