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

import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.exceptions.IllegalMovilizerAppConfigException;
import com.movilizer.connectors.spring.model.impl.MovilizerAppEndpointImpl;
import com.movilizer.connectors.spring.utils.StringUtils;
import com.movilizer.mds.webservice.defaults.DefaultValues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Retrieves the configuration parameters from the Movilizer App class and creates.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
@Component
public class ConfigExtractor {
  protected static final String ENDPOINT_KEY = "endpoints";
  protected static final String ENDPOINT_NAME_KEY = "name";
  protected static final String SYSTEM_ID_KEY = "systemId";
  protected static final String PASSWORD_KEY = "password";
  protected static final String MDS_URL_KEY = "mdsUrl";
  protected static final String UPLOAD_URL_KEY = "uploadUrl";
  protected static final String RESPONSE_QUEUE_KEY = "responseQueue";
  protected static final String POLLING_RATE_KEY = "pollingRate";
  protected static final String CONNECT_TIMEOUT_KEY = "connectionTimeoutInMillis";
  protected static final String RECEIVE_TIMEOUT_KEY = "receiveTimeoutInMillis";
  protected static final String NULL_STRING = "null";
  protected static final Pattern endpointPosPattern = Pattern
      .compile(ENDPOINT_KEY + "\\[(\\d+)\\]");
  protected static Logger logger = LoggerFactory.getLogger(ConfigExtractor.class);

  protected static String endpointIKey(Integer endpointPos, String key) {
    return ENDPOINT_KEY + "[" + endpointPos + "]." + key;
  }

  protected static Integer getEndpointPositionFromKey(String key) {
    Matcher matcher = endpointPosPattern.matcher(key);
    if (matcher.find()) {
      return Integer.parseInt(matcher.group(1));
    } else {
      return -1;
    }
  }

  protected static boolean isMultiEndpoint(Properties properties) {
    for (String propertyName : properties.stringPropertyNames()) {
      if (StringUtils.startsWithIgnoreCase(propertyName, ENDPOINT_KEY)) {
        return true;
      }
    }
    return false;
  }

  protected static Set<Integer> getMultiEndpointPositions(Properties properties) {
    Set<Integer> keys = new HashSet<>();
    Integer pos;
    for (String propertyName : properties.stringPropertyNames()) {
      pos = getEndpointPositionFromKey(propertyName);
      if (pos != -1) {
        keys.add(pos);
      }
    }
    return keys;
  }

  /**
   * Create the {@link MovilizerAppEndpoint}s from a {@link Properties} instance.
   * 
   * @param properties containing the endpoints.
   * @return the list of endpoints.
   */
  public List<MovilizerAppEndpoint> getEndpointsFromProperties(Properties properties) {
    if (isMultiEndpoint(properties)) {
      return getEndpointFromMultiEndpointProperties(properties);
    } else {
      return Collections.singletonList(getEndpointFromSingleEndpointProperties(properties));
    }
  }

  protected MovilizerAppEndpoint getEndpointFromSingleEndpointProperties(Properties properties) {
    return getEndpoint(properties, ENDPOINT_NAME_KEY, SYSTEM_ID_KEY, PASSWORD_KEY, MDS_URL_KEY,
        UPLOAD_URL_KEY, RESPONSE_QUEUE_KEY, POLLING_RATE_KEY, CONNECT_TIMEOUT_KEY,
        RECEIVE_TIMEOUT_KEY);
  }

  protected List<MovilizerAppEndpoint> getEndpointFromMultiEndpointProperties(Properties properties) {
    List<MovilizerAppEndpoint> endpoints = new ArrayList<>();
    for (Integer endpointPosition : getMultiEndpointPositions(properties)) {
      endpoints.add(getIEndpoint(properties, endpointPosition));
    }
    return endpoints;
  }

  protected MovilizerAppEndpoint getIEndpoint(Properties properties, Integer endpointPos) {
    return getEndpoint(properties, endpointIKey(endpointPos, ENDPOINT_NAME_KEY),
        endpointIKey(endpointPos, SYSTEM_ID_KEY), endpointIKey(endpointPos, PASSWORD_KEY),
        endpointIKey(endpointPos, MDS_URL_KEY), endpointIKey(endpointPos, UPLOAD_URL_KEY),
        endpointIKey(endpointPos, RESPONSE_QUEUE_KEY), endpointIKey(endpointPos, POLLING_RATE_KEY),
        endpointIKey(endpointPos, CONNECT_TIMEOUT_KEY),
        endpointIKey(endpointPos, RECEIVE_TIMEOUT_KEY));
  }

  protected MovilizerAppEndpoint getEndpoint(Properties properties, String nameKey,
      String systemIdKey, String passwordKey, String mdsUrlKey, String uploadUrlKey,
      String responseQueueKey, String pollingRateKey,
      String connectionTimeoutInMillisKey, String receiveTimeoutInMillisKey)
      throws IllegalMovilizerAppConfigException {
    // Enforce mandatory properties
    String systemId = String.valueOf(properties.get(systemIdKey));
    if (NULL_STRING.equals(systemId)) {
      throw new IllegalMovilizerAppConfigException("Missing system id in configuration.");
    }
    String password = String.valueOf(properties.get(passwordKey));
    if (NULL_STRING.equals(password)) {
      throw new IllegalMovilizerAppConfigException("Missing password in configuration.");
    }
    // Set defaults
    String name = String.valueOf(properties.get(nameKey));
    if (NULL_STRING.equals(name)) {
      name = UUID.randomUUID().toString();
    }
    String mdsUrl = String.valueOf(properties.get(mdsUrlKey));
    if (NULL_STRING.equals(mdsUrl)) {
      mdsUrl = DefaultValues.MOVILIZER_ENDPOINT.getMdsUrl().toString();
    }
    String uploadUrl = String.valueOf(properties.get(uploadUrlKey));
    if (NULL_STRING.equals(uploadUrl)) {
      uploadUrl = DefaultValues.MOVILIZER_ENDPOINT.getUploadUrl().toString();
    }
    String responseQueue = String.valueOf(properties.get(responseQueueKey));
    if (NULL_STRING.equals(uploadUrl)) {
      responseQueue = ""; // TODO add to default values in movilizer-webservice project
    }
    String pollingRateInSeconds = String.valueOf(properties.get(pollingRateKey));
    if (NULL_STRING.equals(uploadUrl)) {
      pollingRateInSeconds =
          com.movilizer.connectors.spring.requestcycle.DefaultValues.POLLING_RATE_IN_SECONDS
              .toString();
    }
    String connectionTimeoutInMillis = String.valueOf(properties.get(connectionTimeoutInMillisKey));
    if (NULL_STRING.equals(connectionTimeoutInMillis)) {
      connectionTimeoutInMillis = String.valueOf(DefaultValues.CONNECTION_TIMEOUT_IN_MILLIS);
    }
    String receiveTimeoutInMillis = String.valueOf(properties.get(receiveTimeoutInMillisKey));
    if (NULL_STRING.equals(receiveTimeoutInMillis)) {
      receiveTimeoutInMillis = String.valueOf(DefaultValues.RECEIVE_TIMEOUT_IN_MILLIS);
    }
    return new MovilizerAppEndpointImpl(name, Long.parseLong(systemId), password, mdsUrl,
        uploadUrl, responseQueue, Long.parseLong(pollingRateInSeconds),
        Integer.parseInt(connectionTimeoutInMillis),
        Integer.parseInt(receiveTimeoutInMillis));
  }
}
