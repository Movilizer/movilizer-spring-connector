package com.movlizer.connector;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.movilizer.connector"})
@EnableAutoConfiguration
public class FullTestConfig
{

}