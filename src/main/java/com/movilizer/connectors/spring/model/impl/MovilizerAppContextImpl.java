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

package com.movilizer.connectors.spring.model.impl;

import com.movilizer.connectors.spring.model.MovilizerAppContext;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
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
 * @see MovilizerAppContext
 * @since 0.1
 */
public class MovilizerAppContextImpl implements MovilizerAppContext {

  private final String name;
  private final String version;
  private final Properties properties;
  private final Set<String> basePackages;
  private final Set<String> basePackageClasses;
  private List<MovilizerAppEndpoint> endpoints;
  private Map<MovilizerAppEndpoint, List<MovilizerTrigger>> triggerMap;

  /**
   * Create a {@link MovilizerAppContext} given all of the parameters involved.
   * 
   * @param name of the app.
   * @param version of the app.
   * @param properties of the app.
   * @param basePackages of the app.
   * @param basePackageClasses of the app.
   * @param endpoints of the app.
   * @param triggerMap of the app.
   */
  public MovilizerAppContextImpl(String name, String version, Properties properties,
      Set<String> basePackages, Set<String> basePackageClasses,
      List<MovilizerAppEndpoint> endpoints,
      Map<MovilizerAppEndpoint, List<MovilizerTrigger>> triggerMap) {
    this.name = name;
    this.version = version;
    this.properties = properties;
    this.basePackages = basePackages;
    this.basePackageClasses = basePackageClasses;
    this.endpoints = endpoints;
    this.triggerMap = triggerMap;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

  @Override
  public Set<String> basePackages() {
    return basePackages;
  }

  @Override
  public Set<String> basePackageClasses() {
    return basePackageClasses;
  }

  @Override
  public List<MovilizerAppEndpoint> getEndpoints() {
    return endpoints;
  }

  @Override
  public Map<MovilizerAppEndpoint, List<MovilizerTrigger>> getTriggers() {
    return triggerMap;
  }
}
