package com.movilizer.mds.connector.model;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilitas.movilizer.v15.MovilizerStatusMessage;
import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

public class MovilizerRequestSinkTest {
    private static final Logger logger = LoggerFactory.getLogger(MovilizerRequestSinkTest.class);

    private MovilizerConnectorConfig config;
    private MovilizerDistributionService mds;
    private MovilizerRequestSink sink;
    @Mock
    private MovilizerMetricService metricService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        config = new MovilizerConnectorConfig();
        config.setAppName("request-sink-test");
        config.setPushConsolidatedElementsSize(512);
        config.setEndpoint("https://demo.movilizer.com/mds/");
        config.setSystemId(0L);
        config.setPassword("");
        config.setPushingInterval(Duration.ofSeconds(1));
        config.setPollingInterval(Duration.ofSeconds(1));

        mds = config.createMdsInstance();

        sink = MovilizerRequestSink.create(config, "root", metricService);
    }

    @Test
    public void connectionTest() {
        MovilizerRequest request = new MovilizerRequest();
        Flux<MovilizerResponse> responses = sink.responses();
        sink.sendRequest(request);
        sink.sendRequest(request);
        sink.sendRequest(request);

        Flux<String> messages = responses
                .doOnNext(response -> logger.info(mds.responseToString(response)))
                .flatMapIterable(MovilizerResponse::getStatusMessage)
                .map(MovilizerStatusMessage::getMessage);
        StepVerifier.create(messages,1)
                .consumeNextWith( message -> assertThat(message,
                        startsWith("Testcall successful: CUSTOMER_SYSTEM_TYPE_WEBSERVICE")))
                .thenCancel()
                .verify();
    }



}
