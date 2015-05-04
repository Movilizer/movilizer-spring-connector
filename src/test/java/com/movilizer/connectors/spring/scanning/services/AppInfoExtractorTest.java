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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.movilizer.connectors.spring.testdata.apps.CompleteScanningTestApp;
import com.movilizer.connectors.spring.testdata.apps.MinimalScanningTestApp;
import com.movilizer.connectors.spring.testdata.apps.integration.AverageApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public class AppInfoExtractorTest {

  AppInfoExtractor infoExtractor;
  Class<?> minimalApp = MinimalScanningTestApp.class;
  String minimalAppName = "MinimalScanningTestApp";
  String minimalAppVersion = "";
  Set<String> minimalAppBasePackages;
  Set<String> minimalAppBasePackageClasses;

  Class<?> averageApp = AverageApp.class;
  String averageAppName = "average-app";
  String averageAppVersion = "PROD";
  Set<String> averageAppBasePackages;
  String averageAppBasePackages1 =
      "com.movilizer.connectors.spring.testdata.apps.integration.triggers";
  Set<String> averageAppBasePackageClasses;

  Class<?> completeApp = CompleteScanningTestApp.class;
  String completeAppName = "my-super-app";
  String completeAppVersion = "DEV";
  Set<String> completeAppBasePackages;
  String completeAppBasePackages1 = "com.movilizer.connectors.spring.testdata.extra.string";
  Set<String> completeAppBasePackageClasses;
  String completeAppBasePackageClasses1 =
      "com.movilizer.connectors.spring.testdata.extra.classes.one";
  String completeAppBasePackageClasses2 =
      "com.movilizer.connectors.spring.testdata.extra.classes.two";

  @Before
  public void setUp() throws Exception {
    infoExtractor = new AppInfoExtractor();
    minimalAppBasePackages = new HashSet<>();
    minimalAppBasePackageClasses = new HashSet<>();

    averageAppBasePackages = new HashSet<>();
    averageAppBasePackages.add(averageAppBasePackages1);
    averageAppBasePackageClasses = new HashSet<>();

    completeAppBasePackages = new HashSet<>();
    completeAppBasePackages.add(completeAppBasePackages1);
    completeAppBasePackageClasses = new HashSet<>();
    completeAppBasePackageClasses.add(completeAppBasePackageClasses1);
    completeAppBasePackageClasses.add(completeAppBasePackageClasses2);
  }

  @Test
  public void testGetAppNameFromAnnotation() throws Exception {
    String appName = infoExtractor.getAppName(completeApp);
    assertThat(appName, is(notNullValue()));
    assertThat(appName, is(completeAppName));
  }

  @Test
  public void testGetAppNameFromClassName() throws Exception {
    String appName = infoExtractor.getAppName(minimalApp);
    assertThat(appName, is(notNullValue()));
    assertThat(appName, is(minimalAppName));
  }

  @Test
  public void testGetAppVersionFromAnnotation() throws Exception {
    String appVersion = infoExtractor.getAppVersion(completeApp);
    assertThat(appVersion, is(notNullValue()));
    assertThat(appVersion, is(completeAppVersion));
  }

  @Test
  public void testGetAppVersionDefault() throws Exception {
    String appVersion = infoExtractor.getAppVersion(minimalApp);
    assertThat(appVersion, is(notNullValue()));
    assertThat(appVersion, is(minimalAppVersion));
  }

  @Test
  public void testGetAppBasePackagesFromAnnotation() throws Exception {
    Set<String> appBasePackages = infoExtractor.getAppBasePackages(completeApp);
    assertThat(appBasePackages, is(notNullValue()));
    assertThat(appBasePackages.size(), is(completeAppBasePackages.size()));
    assertThat(appBasePackages, hasItem(completeAppBasePackages1));
  }

  @Test
  public void testGetAppBasePackagesFromClassPackage() throws Exception {
    Set<String> appBasePackages = infoExtractor.getAppBasePackages(minimalApp);
    assertThat(appBasePackages, is(notNullValue()));
    assertThat(appBasePackages.size(), is(minimalAppBasePackages.size()));
  }

  @Test
  public void testGetAppBasePackageClassesFromAnnotation() throws Exception {
    Set<String> appBasePackageClasses = infoExtractor.getAppBasePackageClasses(completeApp);
    assertThat(appBasePackageClasses, is(notNullValue()));
    assertThat(appBasePackageClasses.size(), is(completeAppBasePackageClasses.size()));
    assertThat(appBasePackageClasses, hasItem(completeAppBasePackageClasses1));
    assertThat(appBasePackageClasses, hasItem(completeAppBasePackageClasses2));
  }

  @Test
  public void testGetAppBasePackageClassesFromClassPackage() throws Exception {
    Set<String> appBasePackageClasses = infoExtractor.getAppBasePackageClasses(minimalApp);
    assertThat(appBasePackageClasses, is(notNullValue()));
    assertThat(appBasePackageClasses.size(), is(minimalAppBasePackageClasses.size()));
  }
}
