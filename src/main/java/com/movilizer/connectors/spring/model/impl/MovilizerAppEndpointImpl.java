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

import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Threadsafe Movilizer Configuration Object to be shared during runtime.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerAppEndpoint
 * @since 0.1
 */
public class MovilizerAppEndpointImpl implements MovilizerAppEndpoint {

  private AtomicReference<String> name;
  private AtomicLong systemId;
  private AtomicReference<String> password;
  private AtomicReference<String> mdsUrl;
  private AtomicReference<String> uploadUrl;
  private AtomicInteger connectionTimeoutInMillis;
  private AtomicInteger receiveTimeoutInMillis;

  /**
   * Create and {@link MovilizerAppEndpoint} from the given values.
   * 
   * @param name of the endpoint. Must be unique among the other endpoints in the app.
   * @param systemId used for authentication against Movilizer.
   * @param password of the given system id.
   * @param mdsUrl with the address of the Movilizer Distribution Service to use.
   * @param uploadUrl with the address of the Movilizer Document Upload Service to use.
   * @param connectionTimeoutInMillis the connection timeout in milliseconds.
   * @param receiveTimeoutInMillis the incoming receive connection timeout in milliseconds.
   */
  public MovilizerAppEndpointImpl(String name, long systemId, String password, String mdsUrl,
      String uploadUrl, Integer connectionTimeoutInMillis, Integer receiveTimeoutInMillis) {
    this.name = new AtomicReference<>(name);
    this.systemId = new AtomicLong(systemId);
    this.password = new AtomicReference<>(password);
    this.mdsUrl = new AtomicReference<>(mdsUrl);
    this.uploadUrl = new AtomicReference<>(uploadUrl);
    this.connectionTimeoutInMillis = new AtomicInteger(connectionTimeoutInMillis);
    this.receiveTimeoutInMillis = new AtomicInteger(receiveTimeoutInMillis);
  }

  @Override
  public String getName() {
    return name.get();
  }

  @Override
  public Long getSystemId() {
    return systemId.get();
  }

  @Override
  public String getPassword() {
    return password.get();
  }

  @Override
  public String getMdsUrl() {
    return mdsUrl.get();
  }

  @Override
  public String getUploadUrl() {
    return uploadUrl.get();
  }

  @Override
  public Integer getConnectionTimeoutInMillis() {
    return connectionTimeoutInMillis.get();
  }

  @Override
  public Integer getReceiveTimeoutInMillis() {
    return receiveTimeoutInMillis.get();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MovilizerAppEndpointImpl))
      return false;

    MovilizerAppEndpointImpl that = (MovilizerAppEndpointImpl) o;

    return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

  }

  @Override
  public int hashCode() {
    return getName() != null ? getName().hashCode() : 0;
  }
}
