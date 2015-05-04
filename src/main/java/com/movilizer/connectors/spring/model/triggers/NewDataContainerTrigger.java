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

package com.movilizer.connectors.spring.model.triggers;


import com.movilitas.movilizer.v12.MovilizerStatusMessage;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connectors.spring.annotations.triggers.OnNewDataContainers;
import com.movilizer.connectors.spring.model.MovilizerFilterParams;
import com.movilizer.connectors.spring.model.events.MovilizerEvent;
import com.movilizer.connectors.spring.model.events.NewDataContainerEvent;
import com.movilizer.connectors.spring.utils.ClassUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

/**
 * Trigger created from a {@link OnNewDataContainers} annotation. Processes new events of type
 * {@link NewDataContainerEvent}.
 *
 * @author Jesús de Mula Cano
 * @see OnNewDataContainers
 * @see NewDataContainerEvent
 * @since 0.1
 */
public class NewDataContainerTrigger implements MovilizerTrigger {
  private static Log logger = LogFactory.getLog(NewDataContainerTrigger.class);

  private Integer priority;
  private Method method;
  private Object movilizerComponentBean;
  private MovilizerFilterParams filter;
  private ArgMapper mapper;

  /**
   * Create a {@link NewDataContainerTrigger} its priority, filter and target method.
   *
   * @param priority in which the trigger will be executed.
   * @param filter given by the annotation ot the target method.
   * @param triggerTargetMethod method that will be call each time there is an event that matches
   *        the filter.
   * @throws NoSuchMethodException when incorrect method name is given in
   *         {@link #getTriggerSourceMethod()}.
   * @throws IllegalAccessException when there is problem accessing the target method.
   * @see OnNewDataContainers
   * @see NewDataContainerEvent
   */
  public NewDataContainerTrigger(Integer priority, MovilizerFilterParams filter,
      Method triggerTargetMethod) throws NoSuchMethodException, IllegalAccessException {
    ClassUtils.makeAccessible(triggerTargetMethod);
    this.mapper = new PositionalArrayArgMapper(getTriggerSourceMethod(), triggerTargetMethod);
    this.priority = priority;
    this.filter = filter;
    this.method = triggerTargetMethod;
    this.movilizerComponentBean = null;
  }

  private static Method getTriggerSourceMethod() throws NoSuchMethodException {
    return NewDataContainerTrigger.class.getDeclaredMethod("triggerSourceMethod",
        MovilizerFilterParams.class, Long.class, String.class, Calendar.class, Calendar.class,
        String.class, List.class, MovilizerUploadDataContainer.class);
  }

  @Override
  public Object getBean() {
    return movilizerComponentBean;
  }

  @Override
  public void setBean(Object bean) {
    this.movilizerComponentBean = bean;
  }

  @Override
  public Class<? extends MovilizerEvent> getEventClass() {
    return NewDataContainerEvent.class;
  }

  @Override
  public Class<?> getEventAnnotationClass() {
    return OnNewDataContainers.class;
  }

  @Override
  public void fire(MovilizerEvent event) {
    NewDataContainerEvent newDataContainerEventEvent = getSpecificEvent(event);
    if (newDataContainerEventEvent != null) {
      triggerSourceMethod(event.getFilter(), event.getSystemId(), event.getResponseQueue(),
          event.getMdsServerTime(), event.getConnectorServerTime(),
          newDataContainerEventEvent.getRequestAcknowledgeKey(),
          newDataContainerEventEvent.getResponseStatusMessages(),
          newDataContainerEventEvent.getUploadContainer());
    }
  }

  private void triggerSourceMethod(MovilizerFilterParams filter, Long systemId,
      String responseQueue, Calendar mdsServerTime, Calendar connectorServerTime,
      String requestAcknowledgeKey, List<MovilizerStatusMessage> requestStatusMessages,
      MovilizerUploadDataContainer uploadContainer) {

    try {
      ClassUtils.makeAccessible(method);
      ClassUtils.invokeMethod(method, getBean(), mapper.map(filter, systemId, responseQueue,
          mdsServerTime, connectorServerTime, requestAcknowledgeKey, requestStatusMessages,
          uploadContainer));

      // MethodHandles.Lookup lookup = MethodHandles.lookup();
      // ClassUtils.makeAccessible(method);
      // private MethodHandle mh;
      // mh = lookup.unreflect(triggerTargetMethod);
      // mh.bindTo(getBean());
      // mh.invokeWithArguments(mapper.map(filter, systemId, responseQueue, mdsServerTime,
      // connectorServerTime, requestAcknowledgeKey, requestStatusMessages, uploadContainer));
    } catch (IllegalArgumentException e) {
      if (logger.isErrorEnabled()) {
        logger.error("Wrong parameters list inferred for calling the target "
            + "trigger method. Report error to developers", e);
      }
    }
  }

  @Override
  public Integer getPriority() {
    return priority;
  }

  @Override
  public MovilizerFilterParams getFilter() {
    return filter;
  }

  @Override
  public Boolean isDefinedAt(MovilizerFilterParams filterParams) {
    return filter.isEqual(filterParams);
  }

  private NewDataContainerEvent getSpecificEvent(MovilizerEvent event) {
    if (event instanceof NewDataContainerEvent) {
      return (NewDataContainerEvent) event;
    } else {
      if (logger.isWarnEnabled()) {
        logger.warn(String.format("Found unexpected event class. Expected: %s, Given: %s",
            getEventClass().getCanonicalName(), event.getClass().getCanonicalName()));
      }
      return null;
    }
  }
}
