package com.movilizer.connector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Main class and Main Spring Configuration of the Application.
 *
 * @author Pavel Kotlov
 */
@SpringBootApplication
@EnableJpaRepositories
@Import(RepositoryRestMvcConfiguration.class)
@EnableScheduling
public class MovilizerConnectorApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    private static Log logger = LogFactory.getLog(MovilizerConnectorApplication.class);

    public static void main(String[] args) throws Exception {
        logger.debug("Starting Movilizer middleware backend...");
        SpringApplication app = new SpringApplication(MovilizerConnectorApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

    @Bean
    protected ServletContextListener listener() {

        return new ServletContextListener() {

            @Override
            public void contextInitialized(ServletContextEvent sce) {
                logger.info("ServletContext for Movilizer middleware backend initialized");
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                logger.info("ServletContext for Movilizer middleware backend destroyed");
            }

        };
    }
}
