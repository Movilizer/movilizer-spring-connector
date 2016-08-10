package examples;


import com.movilitas.movilizer.v14.MovilizerResponse;
import com.movilizer.connector.java.MovilizerConnectorAPI;
import com.movilizer.connector.java.MovilizerConnectorConfig;
import com.movilizer.connector.java.model.Processor;
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
        SpringApplication app = new SpringApplication(RequestPrinterApp.class);
        app.setShowBanner(false);
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
