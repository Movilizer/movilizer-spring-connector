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
import com.movilizer.connectors.spring.model.MovilizerFilterParams;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Filter parameters for the {@link com.movilizer.connectors.spring.model.triggers.MovilizerTrigger}
 * .
 *
 * @author Jesús de Mula Cano
 * @see MovilizerFilterParams
 * @see com.movilizer.connectors.spring.model.triggers.MovilizerTrigger
 * @since 0.1
 */
public class TriggerFilterParams extends MovilizerFilterParams {
  private Pattern endpoint;
  private Pattern systemId;
  private Pattern responseQueue;
  private Pattern containerUploadPriority;
  private Pattern key;
  private Pattern moveletKey;
  private Pattern moveletKeyExtension;
  private Pattern moveletVersion;
  private Pattern participantKey;
  private Pattern deviceAddress;

  /**
   * Create a {@link MovilizerFilterParams} given all the possible parameters for a
   * {@link com.movilizer.connectors.spring.model.triggers.MovilizerTrigger}.
   * <p/>
   * Once created don't forget to call the {@link #applyDefaults(List)} to adjust the empty regex
   * definitions to valid regex (either adding the default values for endpoints/system ids or
   * setting the wildcards needed to match everything).
   *
   * @param endpoint regex of the trigger.
   * @param systemId regex of the trigger.
   * @param responseQueue regex of the trigger.
   * @param containerUploadPriority regex of the trigger.
   * @param key regex of the trigger.
   * @param moveletKey regex of the trigger.
   * @param moveletKeyExtension regex of the trigger.
   * @param moveletVersion regex of the trigger.
   * @param participantKey regex of the trigger.
   * @param deviceAddress regex of the trigger.
   */
  public TriggerFilterParams(String endpoint, String systemId, String responseQueue,
      String containerUploadPriority, String key, String moveletKey, String moveletKeyExtension,
      String moveletVersion, String participantKey, String deviceAddress) {
    this.endpoint = Pattern.compile(endpoint);
    this.systemId = Pattern.compile(systemId);
    this.responseQueue = Pattern.compile(responseQueue);
    this.containerUploadPriority = Pattern.compile(containerUploadPriority);
    this.key = Pattern.compile(key);
    this.moveletKey = Pattern.compile(moveletKey);
    this.moveletKeyExtension = Pattern.compile(moveletKeyExtension);
    this.moveletVersion = Pattern.compile(moveletVersion);
    this.participantKey = Pattern.compile(participantKey);
    this.deviceAddress = Pattern.compile(deviceAddress);
  }

  /**
   * Substitutes the empty string values given to more adequate ones given the context.
   * <p/>
   * For endpoints names and system ids this function will compile a pattern to match any of the
   * ones contained in endpoints.
   * <p/>
   * For the rest of empty string values, this function will substitute them with {@code ".*"} that
   * will match anything given to it.
   *
   * @param endpoints with the context of the app.
   */
  public void applyDefaults(List<MovilizerAppEndpoint> endpoints) {
    StringBuilder defaultEndpointPattern = new StringBuilder();
    StringBuilder defaultSystemPattern = new StringBuilder();
    for (MovilizerAppEndpoint endpoint : endpoints) {
      defaultEndpointPattern.append(endpoint.getName()).append(OR_REGEX_SYMBOL);
      defaultSystemPattern.append(endpoint.getSystemId()).append(OR_REGEX_SYMBOL);
    }
    // shave off last or symbol and compile if there was no other pattern set before
    if ("".equals(endpoint.pattern()) && defaultEndpointPattern.length() > 0) {
      endpoint =
          Pattern.compile(defaultEndpointPattern.substring(0, defaultEndpointPattern.length()
              - OR_REGEX_SYMBOL.length()));
    }
    if ("".equals(systemId.pattern()) && defaultSystemPattern.length() > 0) {
      systemId =
          Pattern.compile(defaultSystemPattern.substring(0, defaultSystemPattern.length()
              - OR_REGEX_SYMBOL.length()));
    }

    // For the rest we can use wild cards
    if ("".equals(responseQueue.pattern())) {
      responseQueue = Pattern.compile(WILDCARD_REGEX);
    }
    if ("".equals(containerUploadPriority.pattern())) {
      containerUploadPriority = Pattern.compile(WILDCARD_REGEX);
    }
    if ("".equals(key.pattern())) {
      key = Pattern.compile(WILDCARD_REGEX);
    }
    if ("".equals(moveletKey.pattern())) {
      moveletKey = Pattern.compile(WILDCARD_REGEX);
    }
    if ("".equals(moveletKeyExtension.pattern())) {
      moveletKeyExtension = Pattern.compile(WILDCARD_REGEX);
    }
    if ("".equals(moveletVersion.pattern())) {
      moveletVersion = Pattern.compile(WILDCARD_REGEX);
    }
    if ("".equals(participantKey.pattern())) {
      participantKey = Pattern.compile(WILDCARD_REGEX);
    }
    if ("".equals(deviceAddress.pattern())) {
      deviceAddress = Pattern.compile(WILDCARD_REGEX);
    }
  }

  @Override
  public String getEndpoint() {
    return endpoint.pattern();
  }

  @Override
  public String getSystemId() {
    return systemId.pattern();
  }

  @Override
  public String getResponseQueue() {
    return responseQueue.pattern();
  }

  @Override
  public String getContainerUploadPriority() {
    return containerUploadPriority.pattern();
  }

  @Override
  public String getKey() {
    return key.pattern();
  }

  @Override
  public String getMoveletKey() {
    return moveletKey.pattern();
  }

  @Override
  public String getMoveletKeyExtension() {
    return moveletKeyExtension.pattern();
  }

  @Override
  public String getMoveletVersion() {
    return moveletVersion.pattern();
  }

  @Override
  public String getParticipantKey() {
    return participantKey.pattern();
  }

  @Override
  public String getDeviceAddress() {
    return deviceAddress.pattern();
  }

  @Override
  public Boolean isEqual(MovilizerFilterParams populatedFilter) {
    if (!endpoint.matcher(populatedFilter.getEndpoint()).matches()) {
      return false;
    }
    if (!systemId.matcher(populatedFilter.getSystemId()).matches()) {
      return false;
    }
    if (!responseQueue.matcher(populatedFilter.getResponseQueue()).matches()) {
      return false;
    }
    if (!containerUploadPriority.matcher(populatedFilter.getContainerUploadPriority()).matches()) {
      return false;
    }
    if (!key.matcher(populatedFilter.getKey()).matches()) {
      return false;
    }
    if (!moveletKey.matcher(populatedFilter.getMoveletKey()).matches()) {
      return false;
    }
    if (!moveletKeyExtension.matcher(populatedFilter.getMoveletKeyExtension()).matches()) {
      return false;
    }
    if (!moveletVersion.matcher(populatedFilter.getMoveletVersion()).matches()) {
      return false;
    }
    if (!participantKey.matcher(populatedFilter.getParticipantKey()).matches()) {
      return false;
    }
    if (!deviceAddress.matcher(populatedFilter.getDeviceAddress()).matches()) {
      return false;
    }
    return true;
  }
}
