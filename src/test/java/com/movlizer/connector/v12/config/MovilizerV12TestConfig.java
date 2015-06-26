package com.movlizer.connector.v12.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"com.movilizer.modules.sync", "com.movilizer.modules.movilizer.v12",
        "com.movilizer.modules.helper"})
@EntityScan(basePackages = {"com.movilizer.modules"})
@EnableJpaRepositories(basePackages = {"com.movilizer.connector"})
@EnableAutoConfiguration
public class MovilizerV12TestConfig {
}
