/*
 * Copyright 2015 Movilizer GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.movilizer.connectors.spring.init;

import com.movilizer.connectors.spring.model.MovilizerAppContext;
import com.movilizer.connectors.spring.model.MovilizerContext;
import com.movilizer.connectors.spring.model.impl.MovilizerContextImpl;
import com.movilizer.connectors.spring.requestcycle.RequestResponseCycle;
import com.movilizer.connectors.spring.requestcycle.config.AkkaConfig;
import com.movilizer.connectors.spring.scanning.AppScanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * Movilizer spring configuration. In here all the beans needed to manage the connector are declared
 * and initialized.
 * 
 * @author Jesús de Mula Cano
 * @since 0.1
 */
@Configuration
@Import(AkkaConfig.class)
@PropertySource("classpath:META-INF/movilizer/connector-config.yml")
@ComponentScan(basePackageClasses = {AppScanner.class, RequestResponseCycle.class})
public class MovilizerConfig {
  public static final String APP_SCANNER_BEAN = "movilizerAppScanner";
  public static final String MOVILIZER_CONTEXT_BEAN = "movilizerContext";
  private static final Logger logger = LoggerFactory.getLogger(MovilizerConfig.class);
  @Autowired
  private AppScanner appScanner;
  @Value("${movilizer.apps.package}")
  private String appsBasePackage;

  @Bean(name = MOVILIZER_CONTEXT_BEAN)
  @DependsOn(APP_SCANNER_BEAN)
  public MovilizerContext movilizerContext() {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format("Starting Movilizer context with base package scanning in %s...",
          appsBasePackage));
    }
    List<MovilizerAppContext> apps = appScanner.getApps();
    if (logger.isInfoEnabled()) {
      logger.info(String.format("Loaded %s apps", apps.size()));
    }
    return new MovilizerContextImpl(appsBasePackage, apps);
  }
}
