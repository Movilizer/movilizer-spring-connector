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

package com.movilizer.connectors.spring.integration.init;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.movilizer.connectors.spring.MovilizerAppServer;
import com.movilizer.connectors.spring.requestcycle.extension.SpringExtension;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

@RunWith(JUnit4.class)
public class SpringContextInitTest {


  @Before
  public void setUp() throws Exception {

  }

  @Ignore
  @Test
  public void testSpringContextInit() throws Exception {
    ApplicationContext context = SpringApplication.run(MovilizerAppServer.class);

    ActorSystem system = context.getBean(ActorSystem.class);
    final LoggingAdapter log = Logging.getLogger(system, "Application");
    log.info("Starting up test");

    SpringExtension ext = context.getBean(SpringExtension.class);

    // Use the Spring Extension to create props for a named actor bean
    ActorRef supervisor = system.actorOf(ext.props("supervisor"));

    for (int i = 1; i < 10; i++) {
      supervisor.tell(String.format("Hi %d", i), null);
    }
    supervisor.tell(PoisonPill.getInstance(), null);

    while (!supervisor.isTerminated()) {
      Thread.sleep(100);
    }

    log.info("Shutting down test");

    system.shutdown();
    system.awaitTermination();
  }
}
