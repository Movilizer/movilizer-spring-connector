package com.movilizer.mds.connector.model;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import reactor.core.publisher.Flux;

public interface MovilizerRequestSink {
    enum Strategy {PASS_THROUGH, CONSOLIDATE, PASS_THROUGH_SYNC, CONSOLIDATE_SYNC}
    Flux<MovilizerResponse> sendRequest(MovilizerRequest request);
    Flux<MovilizerResponse> responses();
    static MovilizerRequestSink create(MovilizerConnectorConfig config, String name,
                                              MovilizerMetricService metrics) {
        return create(config, name, metrics, Strategy.PASS_THROUGH);
    }

    static MovilizerRequestSink create(MovilizerConnectorConfig config, String name,
                                              MovilizerMetricService metrics, Strategy strategy) {
        switch (strategy) {
            case CONSOLIDATE_SYNC:
                return new ConsolidationSink(config, name, true, metrics);
            case CONSOLIDATE:
                return new ConsolidationSink(config, name, false, metrics);
            case PASS_THROUGH_SYNC:
                return new PassThroughSink(config, name, true, metrics);
            case PASS_THROUGH:
            default:
                return new PassThroughSink(config, name, false, metrics);
        }
    }
}
