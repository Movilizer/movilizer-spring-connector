package com.movilizer.mds.connector;

import com.movilitas.movilizer.v15.MovilizerRequest;
import com.movilitas.movilizer.v15.MovilizerResponse;
import com.movilitas.movilizer.v15.MovilizerStatusMessage;
import com.movilizer.mds.webservice.EndPoint;
import com.movilizer.mds.webservice.Movilizer;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

public class ResponseFluxTest {
    private static final Logger logger = LoggerFactory.getLogger(ResponseFluxTest.class);

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



}
