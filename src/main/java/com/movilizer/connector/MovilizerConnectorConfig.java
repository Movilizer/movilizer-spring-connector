package com.movilizer.connector;

import com.movilizer.connector.exceptions.MovilizerException;
import com.movilizer.connector.utils.AutowireHelper;
import com.movilizer.mds.webservice.EndPoint;
import com.movilizer.mds.webservice.Movilizer;
import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
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
@ComponentScan(basePackages="com.movilizer.connector")
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
	public AutowireHelper autowireHelper()
	{
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

		case "demo":
			mds = Movilizer.buildConf().setEndpoint(EndPoint.DEMO).setOutputEncoding(Charset.forName(charset))
					.getService();
			break;
		case "prod":
			mds = Movilizer.buildConf().setEndpoint(EndPoint.PROD).setOutputEncoding(Charset.forName(charset))
					.getService();
			break;
		case "d":
			mds = Movilizer.buildConf()
					.setEndpoint("https://d.movilizer.com/MovilizerDistributionService/WebService/",
							"https://d.movilizer.com/mds/document")
					.setOutputEncoding(Charset.forName(charset)).getService();
			break;
		case "epcis":
			mds = Movilizer.buildConf()
					.setEndpoint("https://epcis-test.movilizer.com/MovilizerDistributionService/WebService/",
							"https://epcis-test.movilizer.com/mds/document")
					.setOutputEncoding(Charset.forName(charset)).getService();
			break;
		}
		if(mds == null)
		{
			throw new MovilizerException("Configuration Problem. No suitable MovilizerDistributionSevice could be created for the given environment: " + movilizerEnv + ".");
		}
		return mds;
	}
}
