package com.movilizer.mds.connector;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Component;

import static com.codahale.metrics.MetricRegistry.name;

@Component
public class MovilizerMetricServiceImpl implements MovilizerMetricService {
    private static final String MOVILIZER_METRICS_NAMESPACE = "movilizer";
    private static final String SINK_METRICS_NAMESPACE = "sink";
    private static final String SOURCE_METRICS_NAMESPACE = "source";
    private static final String MAPPER_METRICS_NAMESPACE = "mapper";
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String SIZE = "size";
    private static final String MEMORY = "memory";
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";
    private static final String WEBSERVICE = "webservice";
    private static final String TIME = "time";
    private static final String SUBSCRIBERS = "subscribers";
    private static final String ERROR = "error";

    final MovilizerConnectorConfig config;
    final CounterService counterService;
    final GaugeService gaugeService;
    final MetricRegistry registry;

    @Autowired
    public MovilizerMetricServiceImpl(MovilizerConnectorConfig config, CounterService counterService,
                                      GaugeService gaugeService, MetricRegistry registry) {
        this.config = config;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
        this.registry = registry;
    }

    @Override
    public void mapperFromTimeSubmit(Long time) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, MAPPER_METRICS_NAMESPACE, FROM, TIME),
                time);
    }

    @Override
    public void mapperToTimeSubmit(Long time) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, MAPPER_METRICS_NAMESPACE, TO, TIME),
                time);
    }

    @Override
    public Counter createSinkQueueSizeCounter(String sinkName) {
        return registry.counter(name(MOVILIZER_METRICS_NAMESPACE, SINK_METRICS_NAMESPACE,
                sinkName, SIZE));
    }

    @Override
    public void sinkRequestErrorSubmit(String sinkName) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, SINK_METRICS_NAMESPACE,
                sinkName, REQUEST, ERROR), 1);
    }

    @Override
    public void sinkSubscribersIncrement(String sinkName) {
        counterService.increment(name(MOVILIZER_METRICS_NAMESPACE, SINK_METRICS_NAMESPACE,
                sinkName, SUBSCRIBERS));
    }

    @Override
    public void sinkSubscribersReset(String sinkName) {
        counterService.reset(name(MOVILIZER_METRICS_NAMESPACE, SINK_METRICS_NAMESPACE,
                sinkName, SUBSCRIBERS));
    }

    @Override
    public void sinkResponseTimeSubmit(String sinkName, Long time) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, SINK_METRICS_NAMESPACE,
                sinkName, RESPONSE, TIME), time);
    }

    @Override
    public void sinkWebserviceErrorSubmit(String sourceName, Integer errors) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, SINK_METRICS_NAMESPACE,
                sourceName, WEBSERVICE, ERROR), errors);
    }

    @Override
    public void sourceRequestErrorSubmit(String sourceName) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, SOURCE_METRICS_NAMESPACE,
                sourceName, REQUEST, ERROR), 1);
    }

    @Override
    public void sourceSubscribersIncrement(String sourceName) {
        counterService.increment(name(MOVILIZER_METRICS_NAMESPACE, SOURCE_METRICS_NAMESPACE,
                sourceName, SUBSCRIBERS));
    }

    @Override
    public void sourceSubscribersReset(String sourceName) {
        counterService.reset(name(MOVILIZER_METRICS_NAMESPACE, SOURCE_METRICS_NAMESPACE,
                sourceName, SUBSCRIBERS));
    }

    @Override
    public void sourceResponseTimeSubmit(String sourceName, Long time) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, SOURCE_METRICS_NAMESPACE,
                sourceName, RESPONSE, TIME), time);
    }

    @Override
    public void sourceWebserviceErrorSubmit(String sourceName, Integer errors) {
        gaugeService.submit(name(MOVILIZER_METRICS_NAMESPACE, SOURCE_METRICS_NAMESPACE,
                sourceName, WEBSERVICE, ERROR), errors);
    }
}
