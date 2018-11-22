package com.movilizer.mds.connector;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilitas.movilizer.v15.MovilizerStatusMessage;
import com.movilizer.mds.connector.model.MovilizerRequestSink;
import com.movilizer.mds.connector.utils.MovilizerCoreUtils;
import com.movilizer.mds.connector.utils.MovilizerCoreUtilsImpl;
import com.movilizer.mds.webservice.EndPoint;
import com.movilizer.mds.webservice.Movilizer;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class ResponseFluxTest {
    private static final Logger logger = LoggerFactory.getLogger(ResponseFluxTest.class);

    @Mock
    private MovilizerDistributionService mds;

    @Mock
    private MovilizerConnectorConfig config;

    @Mock
    private MovilizerMetricService metrics;

    private MovilizerCoreUtils coreUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        coreUtils = new MovilizerCoreUtilsImpl(config, metrics);

        when(config.createMdsInstance()).thenReturn(mds);
        when(config.getPollingInterval()).thenReturn(Duration.ofSeconds(10));

        doAnswer(invocation -> invocation.getArgumentAt(2, MovilizerRequest.class))
                .when(mds).prepareUploadRequest(anyLong(), anyString(), any(MovilizerRequest.class));

        doAnswer(invocation -> invocation.getArgumentAt(3, MovilizerRequest.class))
                .when(mds).prepareDownloadRequest(anyLong(), anyString(), anyInt(), any(MovilizerRequest.class));
    }

    @Test
    public void simplePollingTest() {
        MovilizerDistributionService mds = Movilizer
                .buildConf()
                .setEndpoint(EndPoint.DEMO)
                .getService();
        Long systemId = 0L;
        String password = "";
        Flux<MovilizerResponse> responses = Flux
                .interval(Duration.ofSeconds(1))
                .map(time -> mds.prepareDownloadRequest(systemId, password,
                        1000, new MovilizerRequest()))
                .flatMap(request -> Mono.fromFuture(mds.getReplyFromCloud(request)))
                .doOnNext(response -> logger.debug(mds.responseToString(response)));

        Flux<String> messages = responses
                .flatMapIterable(MovilizerResponse::getStatusMessage)
                .map(MovilizerStatusMessage::getMessage);

        StepVerifier.create(messages,1)
                .consumeNextWith( message -> assertThat(message,
                        startsWith("Testcall successful: CUSTOMER_SYSTEM_TYPE_WEBSERVICE")))
                .thenCancel()
                .verify();
    }

    @Test
    public void simplePollingSendsAcknowledgeTest() {
        // Create polling flux with mocked/stubbed services
        Flux<MovilizerResponse> responses = coreUtils.responsesSource("sourceName", "responseQueue");

        // Prepare responses from the cloud
        String ack1 = "ack1";
        String ack2 = "ack2";

        MovilizerResponse response1 = new MovilizerResponse();
        response1.setRequestAcknowledgeKey(ack1);

        MovilizerResponse response2 = new MovilizerResponse();
        response2.setRequestAcknowledgeKey(ack2);

        ArgumentCaptor<MovilizerRequest> requestArgumentCaptor = ArgumentCaptor.forClass(MovilizerRequest.class);

        when(mds.getReplyFromCloud(requestArgumentCaptor.capture()))
                .thenReturn(CompletableFuture.completedFuture(response1))
                .thenReturn(CompletableFuture.completedFuture(response2));

        // Run the flux and check that the ack1 from response 1 is in the request 2
        StepVerifier.create(responses,2)
                .consumeNextWith( response -> {
                    assertThat(response.getRequestAcknowledgeKey(), is(ack1));
                })
                .consumeNextWith( response -> {
                    List<MovilizerRequest> requests = requestArgumentCaptor.getAllValues();
                    assertThat(requests.size(), is(2));
                    assertThat(requests.get(1).getRequestAcknowledgeKey(), is(ack1));
                    assertThat(response.getRequestAcknowledgeKey(), is(ack2));
                })
                .thenCancel()
                .verifyThenAssertThat()
                .hasNotDroppedErrors();
    }
}
