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

package com.movilizer.connectors.spring.model.events;

import com.movilitas.movilizer.v12.MovilizerStatusMessage;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.MovilizerFilterParams;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.List;

/**
 * Common attributes among the defined Movilizer web service events.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public interface MovilizerEvent {

  /**
   * Specifies the annotation class used to mark a method as a trigger. This will ease the matching
   * events with triggers when the request-response cycle is running.
   *
   * @return the .class of the annotation used for the event.
   */
  Class<? extends Annotation> getAnnotationClass();

  /**
   * Specifies the actual instance class of the event.
   *
   * @return the instance class of the event.
   */
  Class<? extends MovilizerEvent> getType();

  /**
   * Populated values for the filter that will be compared with the trigger's filter.
   *
   * @return populated filter values.
   */
  MovilizerFilterParams getFilter();

  /**
   * Endpoint where the response came from.
   *
   * @return the endpoint definition.
   */
  MovilizerAppEndpoint getEndpoint();

  /**
   * Getter for the system id of the response.
   *
   * @return the system id of the response.
   */
  Long getSystemId();

  /**
   * Getter for the response queue of the response.
   *
   * @return the response queue of the response.
   */
  String getResponseQueue();

  /**
   * Getter for the time of the Movilizer Cloud of the response.
   *
   * @return the time of the Movilizer Cloud of the response.
   */
  Calendar getMdsServerTime();

  /**
   * Getter for the local system time when the response was received.
   *
   * @return the local system time when the response was received.
   */
  Calendar getConnectorServerTime();

  /**
   * Getter for the acknowledge key of the request.
   *
   * @return the acknowledge key of the request.
   */
  String getRequestAcknowledgeKey();

  /**
   * Getter for the status messages of the response.
   *
   * @return the status messages of the response.
   */
  List<MovilizerStatusMessage> getResponseStatusMessages();
}
