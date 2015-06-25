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

package com.movilizer.connectors.spring.requestcycle.extension;

import akka.actor.Actor;
import akka.actor.Extension;
import akka.actor.Props;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringExtension implements Extension {

  private ApplicationContext applicationContext;

  /**
   * Used to initialize the Spring application context for the extension.
   */
  public void initialize(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Create a Props for the specified actorBeanName using the SpringActorProducer class.
   */
  public Props props(String actorBeanName) {
    return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
  }

  /**
   * Create a Props for the specified type using the SpringActorProducer class.
   */
  public Props props(Class<? extends Actor> type) {
    return Props.create(SpringActorProducer.class, applicationContext, type);
  }
}
