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

package com.movilizer.connectors.spring.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.movilizer.connectors.spring.model.MovilizerAppContext;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.impl.MovilizerAppEndpointImpl;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;
import com.movilizer.connectors.spring.scanning.ClasspathAppScanner;
import com.movilizer.connectors.spring.scanning.services.AppInfoExtractor;
import com.movilizer.connectors.spring.scanning.services.ClassFinder;
import com.movilizer.connectors.spring.scanning.services.ConfigExtractor;
import com.movilizer.connectors.spring.scanning.services.ConfigFinder;
import com.movilizer.connectors.spring.scanning.services.TriggerExtractor;
import com.movilizer.connectors.spring.scanning.services.extractors.OnNewDataContainersTriggerExtractor;
import com.movilizer.connectors.spring.scanning.services.extractors.TriggerAnnotationExtractor;
import com.movilizer.connectors.spring.testdata.apps.integration.AverageApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class ClasspathAppScannerTest {

  Class<?> app = AverageApp.class;
  String appName = "average-app";
  String appVersion = "PROD";
  String endpointName = "demo";
  MovilizerAppEndpoint fakeTestEndpoint;

  String appPackage;
  ClassFinder classFinder;
  ConfigFinder configFinder;
  ConfigExtractor configExtractor;
  AppInfoExtractor appInfoExtractor;
  TriggerExtractor triggerExtractor;
  List<TriggerAnnotationExtractor> triggerExtractors;

  ClasspathAppScanner appScanner;

  @Before
  public void setUp() throws Exception {
    appPackage = app.getPackage().getName();
    classFinder = new ClassFinder();
    appInfoExtractor = new AppInfoExtractor();
    configFinder = new ConfigFinder(appInfoExtractor);
    configExtractor = new ConfigExtractor();
    TriggerAnnotationExtractor extractor = new OnNewDataContainersTriggerExtractor();
    triggerExtractors = new ArrayList<>();
    triggerExtractors.add(extractor);
    triggerExtractor = new TriggerExtractor(classFinder, triggerExtractors);

    appScanner =
        new ClasspathAppScanner(classFinder, configFinder, configExtractor, appInfoExtractor,
            triggerExtractor);
    appScanner.setAppsBasePackage(appPackage);

    fakeTestEndpoint = new MovilizerAppEndpointImpl(endpointName, 1234L, "", "", "", 0, 0);
  }

  @Test
  public void testGetApps() throws Exception {
    List<MovilizerAppContext> apps = appScanner.getApps();
    assertThat("Apps retrieved should always return a value or empty list", apps,
        is(notNullValue()));
    assertThat("There should be one app in the package " + appPackage, apps.size(), is(1));

    MovilizerAppContext app = apps.get(0);
    assertThat("Apps retrieved should never be null", app, is(notNullValue()));
    assertThat("The app name should be " + appName, app.getName(), is(appName));
    assertThat("The app version should properly be " + appVersion, app.getVersion(), is(appVersion));
    assertThat("The app properties should never be null", app.getProperties(), is(notNullValue()));

    List<MovilizerAppEndpoint> endpoints = app.getEndpoints();
    assertThat("The app endpoints should never be null", endpoints, is(notNullValue()));
    assertThat("The app should at least have one endpoint", endpoints.size(), is(greaterThan(0)));

    Map<MovilizerAppEndpoint, List<MovilizerTrigger>> triggersPerEndpoint = app.getTriggers();
    assertThat("The app triggers per endpoint should never be null", triggersPerEndpoint,
        is(notNullValue()));
    assertThat("The app should at least have one endpoint with triggers",
        triggersPerEndpoint.size(),
        is(greaterThan(0)));

    List<MovilizerTrigger> triggers = app.getTriggers().get(fakeTestEndpoint);
    assertThat("The app triggers for an endpoint should never be null", triggers,
        is(notNullValue()));
    assertThat("The test app triggers for the endpoint " + endpointName
        + " should have two endpoint", triggers.size(), is(2));
  }
}
