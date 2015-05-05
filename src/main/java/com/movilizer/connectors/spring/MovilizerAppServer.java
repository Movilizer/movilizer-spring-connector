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

package com.movilizer.connectors.spring;

import com.movilizer.connectors.spring.init.MovilizerConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * This is the default main Spring boot app which will look for all the Movilizer apps defined in
 * the base package in the app.properties (or .yml) file. If more specialized config is needed it
 * can be explicitly in each Movilizer app.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
@SpringBootApplication
@Import(MovilizerConfig.class)
public class MovilizerAppServer {
  private static Log logger = LogFactory.getLog(MovilizerAppServer.class);

  /**
   * Main method to start up the Movilizer Spring Connector.
   *
   * @param args command line sourceMethodArgs (not used at the moment).
   * @throws Exception any uncaught exception in the whole app.
   */
  public static void main(final String[] args) throws Exception {
    if (logger.isInfoEnabled()) {
      logger.info("Starting Movilizer App Server...");
    }
    SpringApplication app = new SpringApplication(MovilizerAppServer.class);
    app.setShowBanner(false);
    app.run(args);
  }
}
