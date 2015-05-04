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

package com.movilizer.connectors.spring.scanning;

import com.movilizer.connectors.spring.annotations.MovilizerApp;
import com.movilizer.connectors.spring.model.MovilizerAppContext;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.exceptions.IllegalMovilizerAppConfigException;
import com.movilizer.connectors.spring.model.impl.MovilizerAppContextImpl;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;
import com.movilizer.connectors.spring.scanning.services.AppInfoExtractor;
import com.movilizer.connectors.spring.scanning.services.ClassFinder;
import com.movilizer.connectors.spring.scanning.services.ConfigExtractor;
import com.movilizer.connectors.spring.scanning.services.ConfigFinder;
import com.movilizer.connectors.spring.scanning.services.TriggerExtractor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Retrieves all the Movilizer apps in the classpath depending of the configuration given.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerAppContext
 * @since 0.1
 */
@Component
public class ClasspathAppScanner implements AppScanner {
  private static Log logger = LogFactory.getLog(ClasspathAppScanner.class);

  @Value("${movilizer.apps.package}")
  private String appsBasePackage;

  private ClassFinder classFinder;
  private ConfigFinder configFinder;
  private ConfigExtractor configExtractor;
  private AppInfoExtractor appInfoExtractor;
  private TriggerExtractor triggerExtractor;

  /**
   * Create a {@link ClasspathAppScanner} giving all its dependencies.
   * 
   * @param classFinder service instance (injected).
   * @param configFinder service instance (injected).
   * @param configExtractor service instance (injected).
   * @param appInfoExtractor service instance (injected).
   * @param triggerExtractor service instance (injected).
   */
  @Autowired
  public ClasspathAppScanner(ClassFinder classFinder, ConfigFinder configFinder,
      ConfigExtractor configExtractor, AppInfoExtractor appInfoExtractor,
      TriggerExtractor triggerExtractor) {
    this.classFinder = classFinder;
    this.configFinder = configFinder;
    this.configExtractor = configExtractor;
    this.appInfoExtractor = appInfoExtractor;
    this.triggerExtractor = triggerExtractor;
  }

  /**
   * Get all the apps in the classpath. (The base package to start to scan is set in
   * {@code META-INF/movilizer/connector-config.yml} in param {@code movilizer.apps.package}.
   *
   * @return all the apps in scope.
   */
  @Override
  public List<MovilizerAppContext> getApps() {
    List<MovilizerAppContext> appsFound = new ArrayList<>();
    if (appsBasePackage != null) {
      Set<Class<?>> classesAnnotatedWithApp =
          classFinder.findClassesAnnotatedWith(MovilizerApp.class, appsBasePackage);
      for (Class annotatedClass : classesAnnotatedWithApp) {
        try {
          appsFound.add(processAnnotatedClassApp(annotatedClass));
        } catch (IllegalMovilizerAppConfigException e) {
          if (logger.isErrorEnabled()) {
            logger.error("Couldn't load app " + annotatedClass.getName(), e);
          }
        }
      }
    }
    return appsFound;
  }

  protected MovilizerAppContext processAnnotatedClassApp(Class<?> annotatedClass)
      throws IllegalMovilizerAppConfigException {
    String appName = appInfoExtractor.getAppName(annotatedClass);
    String version = appInfoExtractor.getAppVersion(annotatedClass);
    Properties properties = configFinder.fromMovilizerAppClass(annotatedClass);
    Set<String> basePackages = appInfoExtractor.getAppBasePackages(annotatedClass);
    Set<String> basePackageClasses = appInfoExtractor.getAppBasePackageClasses(annotatedClass);
    List<MovilizerAppEndpoint> endpoints = configExtractor.getEndpointsFromProperties(properties);
    // Consolidate basePackages
    Set<String> allBasePackages = new HashSet<>(basePackages);
    allBasePackages.addAll(basePackageClasses);
    if (allBasePackages.isEmpty()) {
      allBasePackages.add(appInfoExtractor.getAppDefaultBasePackage(annotatedClass));
    }
    Map<MovilizerAppEndpoint, List<MovilizerTrigger>> triggerMap =
        triggerExtractor.getAppTriggers(endpoints, allBasePackages);

    return new MovilizerAppContextImpl(appName, version, properties, basePackages,
        basePackageClasses, endpoints, triggerMap);
  }

  public void setAppsBasePackage(String appsBasePackage) {
    this.appsBasePackage = appsBasePackage;
  }
}
