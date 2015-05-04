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
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.mds.webservice.defaults.DefaultValues;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Properties;
import java.util.Set;

@RunWith(JUnit4.class)
public class ConfigExtractorTest {

  Properties propertiesMultiEndpointSize1;
  Long systemIdSize1 = 1111L;
  String passwordSize1 = "secret1";
  Properties propertiesMultiEndpointSize3;
  Long systemIdSize3end3 = 3333L;
  String passwordSize3end3 = "secret3";
  Properties propertiesSingleEndpoint;
  String nameSingle = "single";
  Long systemIdSingle = 1234L;
  String passwordSingle = "secret";
  String mdsUrlSingle = "https://movilizer.com/MovilizerDistributionService/WebService/";
  String uploadUrlSingle = "https://movilizer.com/mds/document";
  Integer connectTimeoutSingle = 10000;
  Integer receiveTimeoutSingle = 20000;
  Properties propertiesSingleEndpointComingFromYAML;
  Long systemIdSingleYaml = 4321L;
  String passwordSingleYaml = "supersecret";

  ConfigExtractor configExtractor;

  @Before
  public void setUp() throws Exception {
    configExtractor = new ConfigExtractor();

    propertiesSingleEndpointComingFromYAML = new Properties();
    propertiesSingleEndpointComingFromYAML.put(ConfigExtractor.SYSTEM_ID_KEY,
        String.valueOf(systemIdSingleYaml));
    propertiesSingleEndpointComingFromYAML.put(ConfigExtractor.PASSWORD_KEY, passwordSingleYaml);

    propertiesSingleEndpoint = new Properties();
    propertiesSingleEndpoint.setProperty(ConfigExtractor.ENDPOINT_NAME_KEY,
        String.valueOf(nameSingle));
    propertiesSingleEndpoint.setProperty(ConfigExtractor.SYSTEM_ID_KEY,
        String.valueOf(systemIdSingle));
    propertiesSingleEndpoint.setProperty(ConfigExtractor.PASSWORD_KEY, passwordSingle);
    propertiesSingleEndpoint.setProperty(ConfigExtractor.MDS_URL_KEY, mdsUrlSingle);
    propertiesSingleEndpoint.setProperty(ConfigExtractor.UPLOAD_URL_KEY, uploadUrlSingle);
    propertiesSingleEndpoint.setProperty(ConfigExtractor.CONNECT_TIMEOUT_KEY,
        String.valueOf(connectTimeoutSingle));
    propertiesSingleEndpoint.setProperty(ConfigExtractor.RECEIVE_TIMEOUT_KEY,
        String.valueOf(receiveTimeoutSingle));

    propertiesMultiEndpointSize1 = new Properties();
    propertiesMultiEndpointSize1.setProperty(
        ConfigExtractor.endpointIKey(0, ConfigExtractor.SYSTEM_ID_KEY),
        String.valueOf(systemIdSize1));
    propertiesMultiEndpointSize1.setProperty(
        ConfigExtractor.endpointIKey(0, ConfigExtractor.PASSWORD_KEY), passwordSize1);
    propertiesMultiEndpointSize3 = new Properties();
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(0, ConfigExtractor.SYSTEM_ID_KEY), "1111");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(0, ConfigExtractor.PASSWORD_KEY), "secret1");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(1, ConfigExtractor.SYSTEM_ID_KEY), "2222");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(1, ConfigExtractor.PASSWORD_KEY), "secret2");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(1, ConfigExtractor.MDS_URL_KEY),
        "https://movilizer.com/MovilizerDistributionService/WebService/");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(1, ConfigExtractor.UPLOAD_URL_KEY),
        "https://movilizer.com/mds/document");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(1, ConfigExtractor.CONNECT_TIMEOUT_KEY), "10000");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(1, ConfigExtractor.RECEIVE_TIMEOUT_KEY), "20000");
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(3, ConfigExtractor.SYSTEM_ID_KEY),
        String.valueOf(systemIdSize3end3));
    propertiesMultiEndpointSize3.setProperty(
        ConfigExtractor.endpointIKey(3, ConfigExtractor.PASSWORD_KEY), passwordSize3end3);
  }

  @Test
  public void testIsMultiEndpoint() throws Exception {
    boolean isMultiEndpoint = ConfigExtractor.isMultiEndpoint(propertiesMultiEndpointSize1);
    assertThat(isMultiEndpoint, is(true));

    isMultiEndpoint = ConfigExtractor.isMultiEndpoint(propertiesMultiEndpointSize3);
    assertThat(isMultiEndpoint, is(true));

    isMultiEndpoint = ConfigExtractor.isMultiEndpoint(propertiesSingleEndpoint);
    assertThat(isMultiEndpoint, is(false));

    isMultiEndpoint = ConfigExtractor.isMultiEndpoint(propertiesSingleEndpointComingFromYAML);
    assertThat(isMultiEndpoint, is(false));
  }

  @Test
  public void testGetEndPointFromSingleEndpointProperties() throws Exception {
    MovilizerAppEndpoint endpoint1 =
        configExtractor.getEndpointFromSingleEndpointProperties(propertiesSingleEndpoint);
    assertThat(endpoint1, is(notNullValue()));
    assertThat(endpoint1.getName(), is(nameSingle));
    assertThat(endpoint1.getSystemId(), is(systemIdSingle));
    assertThat(endpoint1.getPassword(), is(passwordSingle));
    assertThat(endpoint1.getMdsUrl(), is(mdsUrlSingle));
    assertThat(endpoint1.getUploadUrl(), is(uploadUrlSingle));
    assertThat(endpoint1.getConnectionTimeoutInMillis(), is(connectTimeoutSingle));
    assertThat(endpoint1.getReceiveTimeoutInMillis(), is(receiveTimeoutSingle));

    MovilizerAppEndpoint endpoint2 =
        configExtractor
            .getEndpointFromSingleEndpointProperties(propertiesSingleEndpointComingFromYAML);
    assertThat(endpoint2, is(notNullValue()));
    assertThat(endpoint2.getSystemId(), is(systemIdSingleYaml));
    assertThat(endpoint2.getPassword(), is(passwordSingleYaml));
    assertThat(endpoint2.getMdsUrl(), is(DefaultValues.MOVILIZER_ENDPOINT.getMdsUrl().toString()));
    assertThat(endpoint2.getUploadUrl(), is(DefaultValues.MOVILIZER_ENDPOINT.getUploadUrl()
        .toString()));
    assertThat(endpoint2.getConnectionTimeoutInMillis(),
        is(DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS));
    assertThat(endpoint2.getReceiveTimeoutInMillis(), is(DefaultValues.RECEIVE_TIMEOUT_IN_MILLIS));
  }

  @Test
  public void testGetEndPoint3FromMultiEndpointProperties() throws Exception {
    MovilizerAppEndpoint endpoint3 = configExtractor.getIEndpoint(propertiesMultiEndpointSize3, 3);
    assertThat(endpoint3, is(notNullValue()));
    assertThat(endpoint3.getSystemId(), is(systemIdSize3end3));
    assertThat(endpoint3.getPassword(), is(passwordSize3end3));
    assertThat(endpoint3.getMdsUrl(), is(DefaultValues.MOVILIZER_ENDPOINT.getMdsUrl().toString()));
    assertThat(endpoint3.getUploadUrl(), is(DefaultValues.MOVILIZER_ENDPOINT.getUploadUrl()
        .toString()));
    assertThat(endpoint3.getConnectionTimeoutInMillis(),
        is(DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS));
    assertThat(endpoint3.getReceiveTimeoutInMillis(), is(DefaultValues.RECEIVE_TIMEOUT_IN_MILLIS));
  }

  @Test
  public void testGetEndpointPositions() throws Exception {
    Set<Integer> positions =
        ConfigExtractor.getMultiEndpointPositions(propertiesMultiEndpointSize3);
    assertThat(positions, is(notNullValue()));
    assertThat(positions.size(), is(3));
    assertThat(positions, hasItems(0, 1, 3));
  }

  @Test
  public void testGetEndpointFromProperties() throws Exception {
    List<MovilizerAppEndpoint> endpoints =
        configExtractor.getEndpointsFromProperties(propertiesMultiEndpointSize3);
    assertThat(endpoints, is(notNullValue()));
    assertThat(endpoints.size(), is(3));
  }
}
