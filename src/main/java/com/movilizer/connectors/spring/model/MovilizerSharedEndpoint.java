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

import java.util.List;

/**
 * Definition for common endpoints used among the apps in the connector to ensure that no replies
 * are missed.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public interface MovilizerSharedEndpoint {

  /**
   * Names given in the app configurations that uniquely identifies the endpoint in the app.
   *
   * @return the endpoint names.
   */
  List<String> getNames();

  /**
   * Add app to the shared endpoint updating to the properties to accordingly. If the timeouts are
   * longer that the current ones they will be updated. If the polling rate is shorter than the
   * current one it will be updated.
   * 
   * @param app to add to the shared endpoint.
   */
  void addApp(MovilizerAppContext app);

  /**
   * Removes an app and its name from the shared endpoint. IMPORTANT: The timeouts and polling rate
   * will NOT be updated.
   * 
   * @param app to remove from the shared endpoint.
   */
  void removeApp(MovilizerAppContext app);

  /**
   * Checks if there's any apps left using the shared endpoint.
   *
   * @return true if at least one app is using the shared endpoint else false.
   */
  boolean hasApps();

  /**
   * System id used to call the Movilizer Web Service.
   *
   * @return the system id of the app.
   */
  Long getSystemId();

  /**
   * Password of the system id used to call the Movilizer Web Service.
   *
   * @return password of the system id of the app.
   */
  String getPassword();

  /**
   * Movilizer Distribution Service address to use for the endpoint.
   *
   * @return the url of the Movilizer Distribution Service.
   */
  String getMdsUrl();

  /**
   * Response queue associated with the endpoint.
   *
   * @return response queue id.
   */
  String getResponseQueue();

  /**
   * Movilizer Document Upload Service address to use for the endpoint.
   *
   * @return the url of the Movilizer Document Upload Service.
   */
  String getUploadUrl();

  /**
   * Time in seconds between polling calls to the endpoint webservice.
   *
   * @return rate in seconds.
   */
  Long getPollingRateInSeconds();

  /**
   * The minimum time in milliseconds among the apps when a connection to the web service or the
   * document upload service will be considered expired.
   *
   * @return the connection timeout in milliseconds.
   */
  Integer getConnectionTimeoutInMillis();

  /**
   * The minimum time in milliseconds among the apps when a incoming receive connection to the web
   * service will be considered expired.
   *
   * @return the incoming receive connection timeout in milliseconds.
   */
  Integer getReceiveTimeoutInMillis();

  /**
   * Uniquely identifies the endpoint as a usable point of view
   * (systemId->MovilizerDistributionService).
   * 
   * @return the key to identify the
   */
  Object consistentHashKey();
}
