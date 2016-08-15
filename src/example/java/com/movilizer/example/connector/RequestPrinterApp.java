package com.movilizer.example.connector;


import com.movilitas.movilizer.v14.MovilizerResponse;
import com.movilizer.connector.MovilizerConnectorAPI;
import com.movilizer.connector.MovilizerConnectorConfig;
import com.movilizer.connector.model.Processor;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Import(MovilizerConnectorConfig.class)
public class RequestPrinterApp {
    private static Log logger = LogFactory.getLog(RequestPrinterApp.class);

    public static void main(String[] args) throws Exception {
        logger.debug("Starting Movilizer middleware backend...");

        /*
         * The problem that is solved by that:
         * ClassCastException ..cannot be cast to com.sun.xml.bind.v2.runtime.reflect.Accessor
         * An alternative way of fixing it was: System.setProperty( "com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
         */
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        SpringApplication app = new SpringApplication(RequestPrinterApp.class);
        app.setShowBanner(false);//app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Component
    private class AppLogic {
        private MovilizerConnectorAPI movilizerConnector;
        private MovilizerDistributionService mds;

        @Autowired
        public AppLogic(MovilizerConnectorAPI movilizerConnector, MovilizerDistributionService mds) {
            this.movilizerConnector = movilizerConnector;
            this.mds = mds;
        }

        @PostConstruct
        void registerCallbacks() {
            logger.info("Registering processor");
            movilizerConnector.registerProcessor(new Processor<MovilizerResponse>() {
                @Override
                public void process(MovilizerResponse response) {
                    logger.info("New response:\n\n" + mds.responseToString(response) + "\n\n");
                }
            }, MovilizerResponse.class);
        }
    }
}
