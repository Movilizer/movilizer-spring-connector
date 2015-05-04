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

/**
 * Configuration object with all the parameters needed to run a Movilizer App. The fields of this
 * app are populated from the files specified in the
 * {@link com.movilizer.connectors.spring.annotations.MovilizerConfig} of the app.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public interface MovilizerAppEndpoint {

  /**
   * Name given in the configuration that uniquely identifies the endpoint in the app.
   * 
   * @return the endpoint name.
   */
  String getName();

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
   * Movilizer Document Upload Service address to use for the endpoint.
   *
   * @return the url of the Movilizer Document Upload Service.
   */
  String getUploadUrl();

  /**
   * Time in milliseconds when a connection to the web service or the document upload service will
   * be considered expired.
   *
   * @return the connection timeout in milliseconds.
   */
  Integer getConnectionTimeoutInMillis();

  /**
   * Time in milliseconds when a incoming receive connection to the web service will be considered
   * expired.
   *
   * @return the incoming receive connection timeout in milliseconds.
   */
  Integer getReceiveTimeoutInMillis();
}
