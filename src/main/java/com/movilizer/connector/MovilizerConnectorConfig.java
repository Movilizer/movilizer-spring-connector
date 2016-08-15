package com.movilizer.connector;

import com.movilizer.connector.exceptions.MovilizerException;
import com.movilizer.connector.utils.AutowireHelper;
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
/*
 * TODO If migrating to Spring Boot 1.4.0 this Annotation causes problems as
 * described here: https://github.com/spring-projects/spring-boot/issues/3439
 *
 * The problem is that the depenencies of the entities are not getting resolved.
 * Without this annotation but with the annotation we are getting issues with
 * the context loading.
 *
 * There are a lot of different proposals however this seems to be only one of
 * the problems connected with the migration. Time invested sever hours. For now
 * we remain with the 1.2.1 release and look for a more stable 1.4 version where
 * only this problem remains so we can try to fix it with possibly a new
 * apporach.
 *
 */
@Import(RepositoryRestMvcConfiguration.class)
@ComponentScan(basePackages = "com.movilizer.connector")
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

	/**
	 * Autowire jpa Beans with spring components.
	 *
	 * @return
	 */
	@Bean
	public AutowireHelper autowireHelper() {
		return AutowireHelper.getInstance();
	}

	// Bean needed to use movilizer web service
	@Bean
	public MovilizerDistributionService mds(@Value("${movilizer.env}") String movilizerEnv,
			@Value("${movilizer.charset}") String charset) throws MalformedURLException {
		EndPoint endpoint = null;
		try {
			endpoint = EndPoint.valueOf(movilizerEnv.toUpperCase());
		} catch (IllegalArgumentException e) {
			return createAlternativeMDS(movilizerEnv, charset);
		}
		return Movilizer.buildConf().setEndpoint(endpoint).setOutputEncoding(Charset.forName(charset)).getService();
	}

	/**
	 * This method shows how alternative endpoints can be configured. Infact the
	 * demo, prod and d environment are supported by the standard
	 * implementation. This code however need improvement since the user needs
	 * to be able to set this from his configuration file if needed.
	 *
	 * @param movilizerEnv
	 * @param charset
	 * @return
	 * @throws MalformedURLException
	 */
	private MovilizerDistributionService createAlternativeMDS(String movilizerEnv, String charset)
			throws MalformedURLException {
		MovilizerDistributionService mds = null;
		switch (movilizerEnv) {

		case "prod":
			mds = Movilizer.buildConf().setEndpoint(EndPoint.PROD).setOutputEncoding(Charset.forName(charset))
					.getService();
			break;
		case "epcis":
			mds = Movilizer.buildConf()
					.setEndpoint("https://epcis-test.movilizer.com/MovilizerDistributionService/WebService/",
							"https://epcis-test.movilizer.com/mds/document")
					.setOutputEncoding(Charset.forName(charset)).getService();
			break;
		}
		if (mds == null) {
			throw new MovilizerException(
					"Configuration Problem. No suitable MovilizerDistributionSevice could be created for the given environment: "
							+ movilizerEnv + ".");
		}
		return mds;
	}
}
