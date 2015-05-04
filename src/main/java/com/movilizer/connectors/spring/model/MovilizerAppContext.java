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

package com.movilizer.connectors.spring.model;


import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Context information that defines a running Movilizer App inside the connector. Required
 * parameters are name and version being the rest optional or filled with defaults.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public interface MovilizerAppContext {

  /**
   * Returns the name of the app. Required and never null. In addition with version fully identifies
   * and app.
   * 
   * @return the name of the app.
   */
  String getName();

  /**
   * Returns the version of the app. Required and never null. In addition with name fully identifies
   * and app.
   *
   * @return the version of the app.
   */
  String getVersion();

  /**
   * Returns the properties loaded while setting up the app.
   *
   * @return the properties instance.
   */
  Properties getProperties();

  /**
   * Component scan base package set to find
   * {@link com.movilizer.connectors.spring.annotations.MovilizerComponent}. If none is given the
   * package where {@link com.movilizer.connectors.spring.annotations.MovilizerApp} is will be used.
   * 
   * @return packages used in the component scan.
   */
  Set<String> basePackages();

  /**
   * Component scan base package classes set to find
   * {@link com.movilizer.connectors.spring.annotations.MovilizerComponent}. If none is given the
   * class that contains {@link com.movilizer.connectors.spring.annotations.MovilizerApp} is will be
   * used.
   *
   * @return classes used in the component scan.
   */
  Set<String> basePackageClasses();

  /**
   * List of {@link MovilizerAppEndpoint} object filled with the properties coming from the
   * {@link com.movilizer.connectors.spring.annotations.MovilizerConfig} annotation.
   * 
   * @return list of endpoints configured for the app.
   */
  List<MovilizerAppEndpoint> getEndpoints();

  /**
   * All the triggers registered in the app by endpoint in no particular order.
   *
   * @return all the triggers available.
   */
  Map<MovilizerAppEndpoint, List<MovilizerTrigger>> getTriggers();
}
