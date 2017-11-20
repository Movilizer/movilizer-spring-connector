package com.movilizer.ps.examples;

import com.movilizer.mds.connector.MovilizerConnectorConfig;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.model.MovilizerRequestSink;
import com.movilizer.mds.connector.utils.MovilizerCoreUtils;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
@Import(MovilizerConnectorConfig.class)
@EnableScheduling
public class SimpleApp {
    private static final Logger logger = LoggerFactory.getLogger(SimpleApp.class);

    @Bean
    MovilizerRequestSink rootSink(MovilizerConnectorConfig config,
                                  MovilizerMetricService metrics) {
        return MovilizerRequestSink.create(config, "root", metrics);
    }

    public static void main(String[] args) {
        logger.info("Starting simple app");
        SpringApplication app = new SpringApplication(SimpleApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = app.run();

        MovilizerCoreUtils utils = context.getBean(MovilizerCoreUtils.class);
        MovilizerConnectorConfig config = context.getBean(MovilizerConnectorConfig.class);
        MovilizerDistributionService mds = config.createMdsInstance();
        MovilizerRequestSink rootSink = context.getBean(MovilizerRequestSink.class);

        logger.info("Starting to send an empty request every 10 seconds after a 5 seconds delay");
        Flux.interval(Duration.ofSeconds(5), Duration.ofSeconds(10))
                .flatMap(time -> rootSink.sendRequest(utils.createUploadRequest()))
                .subscribe(response -> logger.info(mds.responseToString(response)));
    }
}
