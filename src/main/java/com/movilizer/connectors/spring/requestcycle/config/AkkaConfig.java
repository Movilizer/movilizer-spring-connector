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

package com.movilizer.connectors.spring.requestcycle.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import com.movilizer.connectors.spring.init.MovilizerConfig;
import com.movilizer.connectors.spring.requestcycle.extension.SpringExtension;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class AkkaConfig {
  public static final String ACTOR_SYSTEM_BEAN = "actorSystem";
  public static final String SUPERVISOR_BEAN = "movilizerSupervisor";
  private static final Logger logger = LoggerFactory.getLogger(AkkaConfig.class);
  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private SpringExtension springExtension;

  /**
   * Actor system singleton for this application.
   */
  @Bean(name = ACTOR_SYSTEM_BEAN)
  public ActorSystem actorSystem() {
    if (logger.isDebugEnabled()) {
      logger.debug("Starting akka actor system");
    }
    ActorSystem system = ActorSystem.create("MovilizerConnectorProcessing", akkaConfiguration());
    // Initialize the application context in the Akka Spring Extension
    springExtension.initialize(applicationContext);
    return system;
  }

  /**
   * Read configuration from application.conf file.
   */
  @Bean
  public Config akkaConfiguration() {
    return ConfigFactory.load();
  }

  /**
   * Expose the root actor reference for the movilizer response cycle module.
   */
  @Bean
  @DependsOn({ACTOR_SYSTEM_BEAN, MovilizerConfig.MOVILIZER_CONTEXT_BEAN})
  public ActorRef movilizerSupervisorRef() {
    if (logger.isDebugEnabled()) {
      logger.debug("Starting Movilizer Supervisor actor");
    }
    return ((ActorSystem) applicationContext.getBean("actorSystem")).actorOf(springExtension
        .props(SUPERVISOR_BEAN));
  }
}
