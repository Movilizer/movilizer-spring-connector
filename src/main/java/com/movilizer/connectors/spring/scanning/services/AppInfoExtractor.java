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

package com.movilizer.connectors.spring.scanning.services;

import com.movilizer.connectors.spring.annotations.MovilizerApp;
import com.movilizer.connectors.spring.annotations.MovilizerComponentScan;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper service to deal with extracting values from the Movilizer annotations.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerApp
 * @see MovilizerComponentScan
 * @since 0.1
 */
@Component
public class AppInfoExtractor {

  /**
   * Extracts the name of the app from the class given.
   *
   * @param movilizerAppClass the class with the {@link MovilizerApp} annotation.
   * @return the name of the app.
   */
  public String getAppName(Class<?> movilizerAppClass) {
    MovilizerApp appAnnotation = movilizerAppClass.getAnnotation(MovilizerApp.class);
    String appName = inferAppNameFromAppAnnotation(appAnnotation);
    if (appName.isEmpty()) {
      appName = inferAppNameFromAppClassName(movilizerAppClass);
    }
    return appName;
  }

  /**
   * Extracts the version of the app from the class given.
   *
   * @param movilizerAppClass the class with the {@link MovilizerApp} annotation.
   * @return the version of the app.
   */
  public String getAppVersion(Class<?> movilizerAppClass) {
    MovilizerApp appAnnotation = movilizerAppClass.getAnnotation(MovilizerApp.class);
    return appAnnotation.version();
  }

  /**
   * Extracts the base packages where the connector will do component scan for the app.
   *
   * @param movilizerAppClass the class with the {@link MovilizerApp} annotation.
   * @return the base packages to use for the app.
   */
  public Set<String> getAppBasePackages(Class<?> movilizerAppClass) {
    MovilizerComponentScan componentScanAnnotation =
        movilizerAppClass.getAnnotation(MovilizerComponentScan.class);
    Set<String> basePackages = new HashSet<>();
    if (componentScanAnnotation != null) {
      for (String basePackage : componentScanAnnotation.basePackages()) {
        basePackages.add(basePackage);
      }
    }
    return basePackages;
  }

  /**
   * Extracts the base packages where the connector will do component scan for the app.
   *
   * @param movilizerAppClass the class with the {@link MovilizerApp} annotation.
   * @return the base packages to use for the app.
   */
  public Set<String> getAppBasePackageClasses(Class<?> movilizerAppClass) {
    MovilizerComponentScan componentScanAnnotation =
        movilizerAppClass.getAnnotation(MovilizerComponentScan.class);
    Set<String> basePackages = new HashSet<>();
    if (componentScanAnnotation != null) {
      for (Class<?> basePackage : componentScanAnnotation.basePackageClasses()) {
        basePackages.add(basePackage.getPackage().getName());
      }
    }
    return basePackages;
  }

  /**
   * Extracts the default base package for the app.
   *
   * @param movilizerAppClass the class with the {@link MovilizerApp} annotation.
   * @return the base package to use for the app.
   */
  public String getAppDefaultBasePackage(Class<?> movilizerAppClass) {
    MovilizerComponentScan componentScanAnnotation =
        movilizerAppClass.getAnnotation(MovilizerComponentScan.class);
    return movilizerAppClass.getPackage().getName();
  }

  protected String inferAppNameFromAppAnnotation(MovilizerApp movilizerAppAnnotation) {
    String output;
    if (!movilizerAppAnnotation.name().isEmpty()) {
      output = movilizerAppAnnotation.name();
    } else {
      output = movilizerAppAnnotation.value();
    }
    return output;
  }

  protected String inferAppNameFromAppClassName(Class<?> movilizerAppClass) {
    return movilizerAppClass.getSimpleName();
  }
}
