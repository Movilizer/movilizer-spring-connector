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

import com.movilizer.connectors.spring.model.MovilizerFilterParams;

/**
 * Filter parameters for the {@link com.movilizer.connectors.spring.model.events.MovilizerEvent}.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerFilterParams
 * @see com.movilizer.connectors.spring.model.events.MovilizerEvent
 * @since 0.1
 */
public class EventFilterParams extends MovilizerFilterParams {
  private String endpoint;
  private String systemId;
  private String responseQueue;
  private String containerUploadPriority;
  private String key;
  private String moveletKey;
  private String moveletKeyExtension;
  private String moveletVersion;
  private String participantKey;
  private String deviceAddress;

  /**
   * Create a {@link MovilizerFilterParams} given all the possible parameters for a
   * {@link com.movilizer.connectors.spring.model.events.MovilizerEvent}.
   *
   * @param endpoint of the event.
   * @param systemId of the event.
   * @param responseQueue of the event.
   * @param containerUploadPriority of the event.
   * @param key of the event.
   * @param moveletKey of the event.
   * @param moveletKeyExtension of the event.
   * @param moveletVersion of the event.
   * @param participantKey of the event.
   * @param deviceAddress of the event.
   */
  public EventFilterParams(String endpoint, String systemId, String responseQueue,
      String containerUploadPriority, String key, String moveletKey, String moveletKeyExtension,
      String moveletVersion, String participantKey, String deviceAddress) {
    this.endpoint = endpoint;
    this.systemId = systemId;
    this.responseQueue = responseQueue;
    this.containerUploadPriority = containerUploadPriority;
    this.key = key;
    this.moveletKey = moveletKey;
    this.moveletKeyExtension = moveletKeyExtension;
    this.moveletVersion = moveletVersion;
    this.participantKey = participantKey;
    this.deviceAddress = deviceAddress;
  }

  @Override
  public String getEndpoint() {
    return endpoint;
  }

  @Override
  public String getSystemId() {
    return systemId;
  }

  @Override
  public String getResponseQueue() {
    return responseQueue;
  }

  @Override
  public String getContainerUploadPriority() {
    return containerUploadPriority;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public String getMoveletKey() {
    return moveletKey;
  }

  @Override
  public String getMoveletKeyExtension() {
    return moveletKeyExtension;
  }

  @Override
  public String getMoveletVersion() {
    return moveletVersion;
  }

  @Override
  public String getParticipantKey() {
    return participantKey;
  }

  @Override
  public String getDeviceAddress() {
    return deviceAddress;
  }

  @Override
  public Boolean isEqual(MovilizerFilterParams populatedFilter) {
    if (!endpoint.equals(populatedFilter.getEndpoint())) {
      return false;
    }
    if (!systemId.equals(populatedFilter.getSystemId())) {
      return false;
    }
    if (!responseQueue.equals(populatedFilter.getResponseQueue())) {
      return false;
    }
    if (!containerUploadPriority.equals(populatedFilter.getContainerUploadPriority())) {
      return false;
    }
    if (!key.equals(populatedFilter.getKey())) {
      return false;
    }
    if (!moveletKey.equals(populatedFilter.getMoveletKey())) {
      return false;
    }
    if (!moveletKeyExtension.equals(populatedFilter.getMoveletKeyExtension())) {
      return false;
    }
    if (!moveletVersion.equals(populatedFilter.getMoveletVersion())) {
      return false;
    }
    if (!participantKey.equals(populatedFilter.getParticipantKey())) {
      return false;
    }
    if (!deviceAddress.equals(populatedFilter.getDeviceAddress())) {
      return false;
    }
    return true;
  }
}
