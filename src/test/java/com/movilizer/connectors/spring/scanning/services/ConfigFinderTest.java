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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.movilizer.connectors.spring.testdata.apps.CompleteScanningTestApp;
import com.movilizer.connectors.spring.testdata.apps.MinimalScanningTestApp;
import com.movilizer.connectors.spring.testdata.apps.NonExistingConfigTestApp;
import com.movilizer.connectors.spring.testdata.apps.NonValidConfigTestApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Properties;

@RunWith(JUnit4.class)
public class ConfigFinderTest {

  String configPropertiesFilePath = "my-super-app.properties";
  String configYamlFilePath = "classpath:complete-config-manual.yml";
  String minimalConfigYamlFilePath = "minimal-scanning-test-app-config.yaml";

  String basePackageForSearchPattern = "com.movilizer.test";
  String filenameForSearchPattern = "example-app-name";
  String fileExtensionForSearchPattern = ".yml";
  String correctClasspathSearchPattern = "classpath*:com/movilizer/test/**/example-app-name.yml";
  String correctClasspathSearchPatternNoBasePackage = "classpath*:/**/example-app-name.yml";

  Class<?> appClassForAutodiscoveryYaml = MinimalScanningTestApp.class;
  String systemIdYamlKey = ConfigExtractor.SYSTEM_ID_KEY;
  Integer systemIdYaml = 1111; // Yaml config loader is buggy

  Class<?> appClassForAutodiscoveryProperties = CompleteScanningTestApp.class;
  String systemId1PropertiesKey = ConfigExtractor.ENDPOINT_KEY + "[0]."
      + ConfigExtractor.SYSTEM_ID_KEY;
  String systemId1Properties = "1234";
  String systemId2PropertiesKey = ConfigExtractor.ENDPOINT_KEY + "[1]."
      + ConfigExtractor.SYSTEM_ID_KEY;
  String systemId2Properties = "4321";

  Class<?> appClassForNonValidConfig = NonValidConfigTestApp.class;
  Class<?> appClassForNonExistingConfig = NonExistingConfigTestApp.class;

  Class<?> minimalApp = MinimalScanningTestApp.class;
  String systemIdMinimalKey = ConfigExtractor.SYSTEM_ID_KEY;
  Integer systemIdMinimal = 1111; // Yaml config loader is buggy
  String passwordMinimalKey = ConfigExtractor.PASSWORD_KEY;
  String passwordMinimal = "supersecret";

  Class<?> completeApp = CompleteScanningTestApp.class;
  String systemIdCompleteEnd0Key = ConfigExtractor.ENDPOINT_KEY + "[0]."
      + ConfigExtractor.SYSTEM_ID_KEY;
  Integer systemIdCompleteEnd0 = 3333; // Yaml config loader is buggy
  String passwordCompleteEnd0Key = ConfigExtractor.ENDPOINT_KEY + "[0]."
      + ConfigExtractor.PASSWORD_KEY;
  String passwordCompleteEnd0 = "supersecret";
  String systemIdCompleteEnd1Key = ConfigExtractor.ENDPOINT_KEY + "[1]."
      + ConfigExtractor.SYSTEM_ID_KEY;
  Integer systemIdCompleteEnd1 = 6666; // Yaml config loader is buggy
  String passwordCompleteEnd1Key = ConfigExtractor.ENDPOINT_KEY + "[1]."
      + ConfigExtractor.PASSWORD_KEY;
  String passwordCompleteEnd1 = "megasecret";
  String mdsUrlCompleteEnd1Key = ConfigExtractor.ENDPOINT_KEY + "[1]."
      + ConfigExtractor.MDS_URL_KEY;
  String mdsUrlCompleteEnd1 = "https://movilizer.com/MovilizerDistributionService/WebService/";
  String uploadUrlCompleteEnd1Key = ConfigExtractor.ENDPOINT_KEY + "[1]."
      + ConfigExtractor.UPLOAD_URL_KEY;
  String uploadUrlCompleteEnd1 = "https://movilizer.com/mds/document";
  String connectionTimeoutInMillisCompleteEnd1Key = ConfigExtractor.ENDPOINT_KEY + "[1]."
      + ConfigExtractor.CONNECT_TIMEOUT_KEY;
  Integer connectionTimeoutInMillisCompleteEnd1 = 10000; // Yaml config loader is buggy
  String receiveTimeoutInMillisCompleteEnd1Key = ConfigExtractor.ENDPOINT_KEY + "[1]."
      + ConfigExtractor.RECEIVE_TIMEOUT_KEY;
  Integer receiveTimeoutInMillisCompleteEnd1 = 20000; // Yaml config loader is buggy

  ConfigFinder configFinder;

  @Before
  public void setUp() throws Exception {
    configFinder = new ConfigFinder(new AppInfoExtractor());
  }

  @Test
  public void testCheckCorrectFileTypeForPropertiesFile() throws Exception {
    boolean isProperties = configFinder.isPropertiesFile(configPropertiesFilePath);
    assertThat(isProperties, is(true));

    isProperties = configFinder.isPropertiesFile(configYamlFilePath);
    assertThat(isProperties, is(false));
  }

  @Test
  public void testCheckCorrectFileTypeForYamlFile() throws Exception {
    boolean isProperties = configFinder.isYamlFile(configYamlFilePath);
    assertThat(isProperties, is(true));

    isProperties = configFinder.isYamlFile(minimalConfigYamlFilePath);
    assertThat(isProperties, is(true));

    isProperties = configFinder.isYamlFile(configPropertiesFilePath);
    assertThat(isProperties, is(false));
  }

  @Test
  public void testCorrectSearchPattern() throws Exception {
    String searchPattern =
        configFinder.getSearchPattern(filenameForSearchPattern, fileExtensionForSearchPattern,
            basePackageForSearchPattern);
    assertThat(searchPattern, is(notNullValue()));
    assertThat(searchPattern, is(correctClasspathSearchPattern));
  }

  @Test
  public void testCorrectSearchPatternNoBasePackage() throws Exception {
    String searchPattern =
        configFinder.getSearchPattern(filenameForSearchPattern, fileExtensionForSearchPattern, "");
    assertThat(searchPattern, is(notNullValue()));
    assertThat(searchPattern, is(correctClasspathSearchPatternNoBasePackage));
  }

  @Test
  public void testPropertiesFileIsFound() throws Exception {
    Properties properties = configFinder.findPropertiesResource(configPropertiesFilePath);
    assertThat(properties, is(notNullValue()));
    assertThat(properties.propertyNames().hasMoreElements(), is(true));
  }

  @Test
  public void testYamlFileIsFound() throws Exception {
    Properties properties = configFinder.findPropertiesResource(configYamlFilePath);
    assertThat(properties, is(notNullValue()));
    assertThat(properties.propertyNames().hasMoreElements(), is(true));
  }

  @Test
  public void testAutomaticConfigDiscoveryWithYamlFile() throws Exception {
    Properties properties = configFinder.automaticConfigFileDiscovery(appClassForAutodiscoveryYaml);
    assertThat(properties, is(notNullValue()));
    assertThat((Integer) properties.get(systemIdYamlKey), is(systemIdYaml));
  }

  @Test
  public void testAutomaticConfigDiscoveryWithPropertiesFile() throws Exception {
    Properties properties =
        configFinder.automaticConfigFileDiscovery(appClassForAutodiscoveryProperties);
    assertThat(properties, is(notNullValue()));
    assertThat(properties.getProperty(systemId1PropertiesKey), is(systemId1Properties));
    assertThat(properties.getProperty(systemId2PropertiesKey), is(systemId2Properties));
  }

  @Test
  public void testNonNullPropertiesWhenFileIsNotValidType() throws Exception {
    Properties properties = configFinder.fromMovilizerAppClass(appClassForNonValidConfig);
    assertThat(properties, is(notNullValue()));
    assertThat(properties.propertyNames().hasMoreElements(), is(false));
  }

  @Test
  public void testNonNullPropertiesWhenFileDoesNotExists() throws Exception {
    Properties properties = configFinder.fromMovilizerAppClass(appClassForNonExistingConfig);
    assertThat(properties, is(notNullValue()));
    assertThat(properties.propertyNames().hasMoreElements(), is(false));
  }

  @Test
  public void testGetPropertiesFromMinimalApp() throws Exception {
    Properties properties = configFinder.fromMovilizerAppClass(minimalApp);
    assertThat(properties, is(notNullValue()));
    assertThat((Integer) properties.get(systemIdMinimalKey), is(systemIdMinimal));
    assertThat((String) properties.get(passwordMinimalKey), is(passwordMinimal));
  }

  @Test
  public void testGetPropertiesFromCompleteApp() throws Exception {
    Properties properties = configFinder.fromMovilizerAppClass(completeApp);
    assertThat(properties, is(notNullValue()));
    assertThat((Integer) properties.get(systemIdCompleteEnd0Key), is(systemIdCompleteEnd0));
    assertThat((String) properties.get(passwordCompleteEnd0Key), is(passwordCompleteEnd0));
    assertThat((Integer) properties.get(systemIdCompleteEnd1Key), is(systemIdCompleteEnd1));
    assertThat((String) properties.get(passwordCompleteEnd1Key), is(passwordCompleteEnd1));
    assertThat((String) properties.get(mdsUrlCompleteEnd1Key), is(mdsUrlCompleteEnd1));
    assertThat((String) properties.get(uploadUrlCompleteEnd1Key), is(uploadUrlCompleteEnd1));
    assertThat((Integer) properties.get(connectionTimeoutInMillisCompleteEnd1Key),
        is(connectionTimeoutInMillisCompleteEnd1));
    assertThat((Integer) properties.get(receiveTimeoutInMillisCompleteEnd1Key),
        is(receiveTimeoutInMillisCompleteEnd1));
  }
}
