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

import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;

/**
 * Comparator used for the {@link MovilizerTrigger} and
 * {@link com.movilizer.connectors.spring.model.events.MovilizerEvent} to assess when a trigger
 * needs to be fired. The different events should have their own implementations depending on which
 * parameters are specific for the given event/trigger.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerTrigger
 * @see com.movilizer.connectors.spring.model.events.MovilizerEvent
 * @since 0.1
 */
public abstract class MovilizerFilterParams {

  /**
   * OR symbol used in the regex patterns.
   */
  public static final String OR_REGEX_SYMBOL = "|";

  /**
   * Wildcard pattern used when a empty string is given in a regex pattern.
   */
  public static final String WILDCARD_REGEX = ".*";

  /**
   * Endpoint name/s associated with the filter.
   * 
   * @return endpoint names or the regex pattern.
   */
  public abstract String getEndpoint();

  /**
   * System id/s associated with the filter.
   *
   * @return system ids or the regex pattern.
   */
  public abstract String getSystemId();

  /**
   * Response queue/s associated with the filter.
   *
   * @return response queues or the regex pattern.
   */
  public abstract String getResponseQueue();

  /**
   * Container upload priority associated with the filter.
   *
   * @return container upload priority or the regex pattern.
   */
  public abstract String getContainerUploadPriority();

  /**
   * Datacontainer key associated with the filter.
   *
   * @return datacontainer key or the regex pattern.
   */
  public abstract String getKey();

  /**
   * Movelet key associated with the filter.
   *
   * @return movelet key or the regex pattern.
   */
  public abstract String getMoveletKey();

  /**
   * Movelet key extension associated with the filter.
   *
   * @return movelet key extension or the regex pattern.
   */
  public abstract String getMoveletKeyExtension();

  /**
   * Movelet version associated with the filter.
   *
   * @return movelet version or the regex pattern.
   */
  public abstract String getMoveletVersion();

  /**
   * Participant key associated with the filter.
   *
   * @return participant key or the regex pattern.
   */
  public abstract String getParticipantKey();

  /**
   * Device address associated with the filter.
   *
   * @return device address or the regex pattern.
   */
  public abstract String getDeviceAddress();

  /**
   * It compares all non null properties of the instance with their counterparts in the parameter.
   * If they don't share the same class it should return false.
   *
   * @param populatedFilter the filter to compare to.
   * @return true if all non null instance filter are the same as the ones of the parameter.
   */
  public abstract Boolean isEqual(MovilizerFilterParams populatedFilter);
}
