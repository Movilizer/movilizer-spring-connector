package com.movilizer.connector.java;

import com.movilizer.mds.webservice.EndPoint;
import com.movilizer.mds.webservice.Movilizer;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.MalformedURLException;
import java.nio.charset.Charset;


@Configuration
@EnableAutoConfiguration
@EnableScheduling
@EnableJpaRepositories
@Import(RepositoryRestMvcConfiguration.class)
//@PropertySource("classpath:config/movilizer.yml")
@ComponentScan
public class MovilizerConnectorConfig {
    private static Log logger = LogFactory.getLog(MovilizerConnectorConfig.class);

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

    //Bean needed to use movilizer web service
    @Bean
    MovilizerDistributionService mds(@Value("${movilizer.env}") String movilizerEnv, @Value("${movilizer.charset}") String charset) throws MalformedURLException {
        MovilizerDistributionService mds = null;
        switch (movilizerEnv) {
            case "demo":
                mds = Movilizer.buildConf().setEndpoint(EndPoint.DEMO).setOutputEncoding(Charset.forName(charset)).getService();
                break;
            case "prod":
                mds = Movilizer.buildConf().setEndpoint(EndPoint.PROD).setOutputEncoding(Charset.forName(charset)).getService();
                break;
            case "d":
                mds = Movilizer.buildConf()
                        .setEndpoint("https://d.movilizer.com/MovilizerDistributionService/WebService/", "https://d.movilizer.com/mds/document")
                        .setOutputEncoding(Charset.forName(charset))
                        .getService();
                break;
        }
        return mds;
    }
}
