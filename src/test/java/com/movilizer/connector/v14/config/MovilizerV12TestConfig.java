package com.movilizer.connector.v14.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"com.movilizer.connector"})
@EntityScan(basePackages = {"com.movilizer.connector.java.persistence.entities"})
@EnableJpaRepositories
@EnableAutoConfiguration
public class MovilizerV12TestConfig {
}
