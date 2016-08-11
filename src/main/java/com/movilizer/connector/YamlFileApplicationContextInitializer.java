package com.movilizer.connector;

import java.io.IOException;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

public class YamlFileApplicationContextInitializer
		implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		try {
			YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();

			Resource resource = applicationContext.getResource("classpath:config/application.yml");
			PropertySource<?> yamlProperties = sourceLoader.load("application", resource, null);
			applicationContext.getEnvironment().getPropertySources().addFirst(yamlProperties);

			String environmentSuffix = yamlProperties.getProperty("spring.profiles.active").toString();
			resource = applicationContext
					.getResource("classpath:config/application-" + environmentSuffix + ".yml");
			PropertySource<?> yamlEnvProperties = sourceLoader.load("yamlEnvProperties", resource, null);
			applicationContext.getEnvironment().getPropertySources().addFirst(yamlEnvProperties);


		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
