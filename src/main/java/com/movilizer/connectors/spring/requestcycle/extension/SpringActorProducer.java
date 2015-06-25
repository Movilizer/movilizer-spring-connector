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
import akka.actor.IndirectActorProducer;

import org.springframework.context.ApplicationContext;

public class SpringActorProducer implements IndirectActorProducer {

  private final ApplicationContext applicationContext;
  private String actorBeanName;
  private Class<? extends Actor> type;

  public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
    this.applicationContext = applicationContext;
    this.actorBeanName = actorBeanName;
  }

  public SpringActorProducer(ApplicationContext applicationContext, Class<? extends Actor> type) {
    this.applicationContext = applicationContext;
    this.type = type;
  }

  @Override
  public Actor produce() {
    if (actorBeanName != null) {
      //By Name wiring;
      return (Actor) applicationContext.getBean(actorBeanName);
    }
    //By Type wiring;
    return applicationContext.getBean(type);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<? extends Actor> actorClass() {
    if (actorBeanName != null) {
      //By Name wiring;
      return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
    }
    //By Type wiring;
    return type;

  }
}
