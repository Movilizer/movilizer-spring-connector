package com.movilizer.connector;

import com.movilizer.connector.java.MovilizerConnectorConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Main class and Main Spring Configuration of the Application.
 *
 * @author Pavel Kotlov
 */
@SpringBootApplication
@Import(MovilizerConnectorConfig.class)
public class MovilizerConnectorApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    private static Log logger = LogFactory.getLog(MovilizerConnectorApplication.class);

    public static void main(String[] args) throws Exception {
        logger.debug("Starting Movilizer middleware backend...");
        SpringApplication app = new SpringApplication(MovilizerConnectorApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }
}
