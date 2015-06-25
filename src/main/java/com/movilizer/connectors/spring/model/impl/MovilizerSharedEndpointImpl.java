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
import com.movilizer.connectors.spring.model.MovilizerSharedEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Definition for common endpoints used among the apps in the connector to ensure that no replies
 * are missed.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public class MovilizerSharedEndpointImpl implements MovilizerSharedEndpoint, Serializable {
  private static final long serialVersionUID = 4482686585255524397L;
  private static Logger logger = LoggerFactory.getLogger(MovilizerSharedEndpointImpl.class);
  private final Long key;
  private final Long systemId;
  private final String password;
  private final String mdsUrl;
  private final String uploadUrl;
  private final String responseQueue;
  private Long pollingRateInSeconds;
  private Integer connectionTimeoutInMillis;
  private Integer receiveTimeoutInMillis;
  private List<String> names;
  private List<MovilizerAppContext> apps;

  public MovilizerSharedEndpointImpl(MovilizerAppContext app, MovilizerAppEndpoint endpoint) {
    key = calculateKey(endpoint.getMdsUrl(), endpoint.getSystemId(), endpoint.getResponseQueue());
    systemId = endpoint.getSystemId();
    password = endpoint.getPassword();
    mdsUrl = endpoint.getMdsUrl();
    uploadUrl = endpoint.getUploadUrl();
    responseQueue = endpoint.getResponseQueue();
    pollingRateInSeconds = endpoint.getPollingRateInSeconds();
    connectionTimeoutInMillis = endpoint.getConnectionTimeoutInMillis();
    receiveTimeoutInMillis = endpoint.getReceiveTimeoutInMillis();
    names = new ArrayList<>();
    names.add(endpoint.getName());
    apps = new ArrayList<>();
    apps.add(app);
    if (logger.isDebugEnabled()) {
      logger
          .debug(String
              .format(
                  "Created shared endpoint %s#%d-%s with initial Movilizer app '%s' and endpoint name '%s'",
                  endpoint.getMdsUrl(), endpoint.getSystemId(), endpoint.getResponseQueue(),
                  app.getName(), endpoint.getName()));
    }
  }

  private static Long calculateKey(String mdsUrl, Long systemId, String responseQueue) {
    return mdsUrl.hashCode() + systemId + responseQueue.hashCode();
  }

  @Override
  public void addApp(MovilizerAppContext app) {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format("Trying to add Movilizer app '%s' to shared endpoint %s#%d-%s",
          app.getName(), mdsUrl, systemId, responseQueue));
    }
    for (MovilizerAppEndpoint endpoint : app.getEndpoints()) {
      if (key.equals(calculateKey(endpoint.getMdsUrl(), endpoint.getSystemId(),
          endpoint.getResponseQueue()))) {
        apps.add(app);
        getNames().add(endpoint.getName());
        updateTimeouts(endpoint);
        updatePollingRate(endpoint);
        if (logger.isDebugEnabled()) {
          logger.debug(String.format(
              "Added Movilizer app '%s' with endpoint name '%s' to shared endpoint %s#%d-%s",
              app.getName(), endpoint.getName(), endpoint.getMdsUrl(), endpoint.getSystemId(),
              endpoint.getResponseQueue()));
        }
      }
    }
  }

  private void updateTimeouts(MovilizerAppEndpoint endpoint) {
    // Connection
    Integer currentConnectionTimeout = getConnectionTimeoutInMillis();
    Integer newConnectionTimeout = endpoint.getConnectionTimeoutInMillis();
    if (newConnectionTimeout > currentConnectionTimeout) {
      connectionTimeoutInMillis = newConnectionTimeout;
      if (logger.isDebugEnabled()) {
        logger
            .debug(String
                .format(
                    "Updated connection timeout from %d millis to %d millis with endpoint '%s' in shared endpoint %s#%d-%s",
                    currentConnectionTimeout, newConnectionTimeout, endpoint.getName(), mdsUrl,
                    systemId, responseQueue));
      }
    }
    // Receive
    Integer currentReceiveTimeout = getReceiveTimeoutInMillis();
    Integer newReceiveTimeout = endpoint.getReceiveTimeoutInMillis();
    if (newReceiveTimeout > currentReceiveTimeout) {
      receiveTimeoutInMillis = newReceiveTimeout;
      if (logger.isDebugEnabled()) {
        logger
            .debug(String
                .format(
                    "Updated receive timeout from %d millis to %d millis with endpoint '%s' in shared endpoint %s#%d-%s",
                    currentReceiveTimeout, newReceiveTimeout, endpoint.getName(), mdsUrl, systemId,
                    responseQueue));
      }
    }
  }

  private void updatePollingRate(MovilizerAppEndpoint endpoint) {
    Long currentRate = getPollingRateInSeconds();
    Long newRate = endpoint.getPollingRateInSeconds();
    if (newRate > currentRate) {
      pollingRateInSeconds = newRate;
      if (logger.isDebugEnabled()) {
        logger
            .debug(String
                .format(
                    "Updated polling rate from %d seconds to %d seconds with endpoint '%s' in shared endpoint %s#%d-%s",
                    currentRate, newRate, endpoint.getName(), mdsUrl, systemId, responseQueue));
      }
    }
  }

  @Override
  public void removeApp(MovilizerAppContext app) {
    apps.remove(app);
    getNames().remove(app.getName());
  }

  @Override
  public boolean hasApps() {
    return apps.isEmpty();
  }

  @Override
  public List<String> getNames() {
    return names;
  }

  @Override
  public Long getSystemId() {
    return systemId;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getMdsUrl() {
    return mdsUrl;
  }

  @Override
  public String getUploadUrl() {
    return uploadUrl;
  }

  @Override
  public Integer getConnectionTimeoutInMillis() {
    return connectionTimeoutInMillis;
  }

  @Override
  public String getResponseQueue() {
    return responseQueue;
  }

  @Override
  public Integer getReceiveTimeoutInMillis() {
    return receiveTimeoutInMillis;
  }

  @Override
  public Object consistentHashKey() {
    return key;
  }

  @Override
  public Long getPollingRateInSeconds() {
    return pollingRateInSeconds;
  }
}
