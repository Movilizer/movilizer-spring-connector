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

import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilitas.movilizer.v12.MovilizerStatusMessage;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connectors.spring.annotations.triggers.OnNewDataContainers;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.MovilizerFilterParams;
import com.movilizer.connectors.spring.model.impl.EventFilterParams;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.List;

/**
 * New Datacontainers coming from an specific Movelet with specific container upload priority.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public class NewDataContainerEvent implements MovilizerEvent {
  private MovilizerFilterParams filter;
  private MovilizerAppEndpoint endpoint;
  private Long systemId;
  private String responseQueue;
  private Calendar mdsServerTime;
  private Calendar connectorServerTime;
  private String requestAcknowledgeKey;
  private List<MovilizerStatusMessage> requestStatusMessages;
  private MovilizerUploadDataContainer uploadContainer;

  /**
   * Creates a new {@link MovilizerEvent} originated from new datacontainers in the incoming
   * request.
   *
   * @param endpoint where the request came from.
   * @param response that contains the datacontainers.
   * @param uploadContainer the actual datacontainer.
   * @param connectorServerTime the local time when the response was received.
   *
   * @see MovilizerEvent
   */
  public NewDataContainerEvent(MovilizerAppEndpoint endpoint, MovilizerResponse response,
      MovilizerUploadDataContainer uploadContainer, Calendar connectorServerTime) {
    this.endpoint = endpoint;
    systemId = response.getSystemId();
    responseQueue = response.getResponseQueue();
    mdsServerTime = response.getCurrentServerTime().toGregorianCalendar();
    this.connectorServerTime = connectorServerTime;
    requestAcknowledgeKey = response.getRequestAcknowledgeKey();
    requestStatusMessages = response.getStatusMessage();
    this.uploadContainer = uploadContainer;
    this.filter =
        new EventFilterParams(endpoint.getName(), String.valueOf(systemId), responseQueue,
            String.valueOf(uploadContainer.getContainerUploadPriority()), uploadContainer
                .getContainer().getKey(), uploadContainer.getContainer().getMoveletKey(),
            uploadContainer.getContainer().getMoveletKeyExtension(), String.valueOf(uploadContainer
                .getContainer().getMoveletVersion()), uploadContainer.getContainer()
                .getParticipantKey(), uploadContainer.getContainer().getDeviceAddress());
  }

  @Override
  public Class<? extends Annotation> getAnnotationClass() {
    return OnNewDataContainers.class;
  }

  @Override
  public Class<? extends MovilizerEvent> getType() {
    return NewDataContainerEvent.class;
  }

  @Override
  public MovilizerFilterParams getFilter() {
    return filter;
  }

  @Override
  public MovilizerAppEndpoint getEndpoint() {
    return endpoint;
  }

  @Override
  public Long getSystemId() {
    return systemId;
  }

  @Override
  public String getResponseQueue() {
    return responseQueue;
  }

  @Override
  public Calendar getMdsServerTime() {
    return mdsServerTime;
  }

  /**
   * Returns the local server time when the response was received.
   *
   * @return datacontainer that generated the event.
   */
  public Calendar getConnectorServerTime() {
    return connectorServerTime;
  }

  @Override
  public String getRequestAcknowledgeKey() {
    return requestAcknowledgeKey;
  }

  @Override
  public List<MovilizerStatusMessage> getResponseStatusMessages() {
    return requestStatusMessages;
  }

  /**
   * Returns the datacontainer that generated the event.
   *
   * @return datacontainer that generated the event.
   */
  public MovilizerUploadDataContainer getUploadContainer() {
    return uploadContainer;
  }
}
