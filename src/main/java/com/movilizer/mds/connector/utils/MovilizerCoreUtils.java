package com.movilizer.mds.connector.utils;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;

import com.movilizer.mds.connector.model.MovilizerRequestSink;

import reactor.core.publisher.Flux;


public interface MovilizerCoreUtils {

    MovilizerRequest createUploadRequest();

    Flux<MovilizerResponse> responsesSource(String sourceName);

    Flux<MovilizerResponse> responsesSource(String sourceName, String responseQueue);

    MovilizerRequestSink createRequestSink(String name);

    MovilizerRequestSink createRequestSink(String name, MovilizerRequestSink.Strategy strategy);

    MovilizerRequestSink createRequestSink(String name, String responseQueue);

    MovilizerRequestSink createRequestSink(String name, String responseQueue, MovilizerRequestSink.Strategy strategy);
}
