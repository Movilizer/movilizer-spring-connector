package com.movilizer.mds.connector;

import com.codahale.metrics.Counter;

public interface MovilizerMetricService {
    void mapperFromTimeSubmit(Long time);

    void mapperToTimeSubmit(Long time);

    Counter createSinkQueueSizeCounter(String sinkName);

    void sinkRequestErrorSubmit(String sinkName);

    void sinkSubscribersIncrement(String sinkName);

    void sinkSubscribersReset(String sinkName);

    void sinkResponseTimeSubmit(String sinkName, Long time);

    void sinkWebserviceErrorSubmit(String sourceName, Integer errors);

    void sourceRequestErrorSubmit(String sourceName);

    void sourceSubscribersIncrement(String sourceName);

    void sourceSubscribersReset(String sourceName);

    void sourceResponseTimeSubmit(String sourceName, Long time);

    void sourceWebserviceErrorSubmit(String sourceName, Integer errors);
}
