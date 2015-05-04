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

import com.movilizer.connectors.spring.model.MovilizerFilterParams;
import com.movilizer.connectors.spring.model.events.MovilizerEvent;

/**
 * Triggers to act upon {@link MovilizerEvent} coming from the Movilizer web service. Instances of
 * this class are the materialization of the methods annotated in the
 * {@link com.movilizer.connectors.spring.annotations.MovilizerComponent} and will be the ones that
 * the connector uses to orchestrate the interactions with the web service.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerEvent
 * @see com.movilizer.connectors.spring.model.events.MovilizerEvent
 * @since 0.1
 */
public interface MovilizerTrigger {

  /**
   * Spring bean that contains the method to be run when the trigger is fired.
   * 
   * @return spring bean with the method to be run.
   */
  Object getBean();

  /**
   * Spring bean that contains the method to be run when the trigger is fired.
   * 
   * @param bean the spring bean of the Movilizer component
   */
  void setBean(Object bean);

  /**
   * Class of the internal {@link MovilizerEvent} instance to be able to group triggers by its event
   * type.
   *
   * @return internal event class that the trigger manages.
   */
  Class<? extends MovilizerEvent> getEventClass();

  /**
   * Annotation class used to define the trigger in the
   * {@link com.movilizer.connectors.spring.annotations.MovilizerComponent}.
   *
   * @return annotation class defined for the event that the trigger manages.
   */
  Class<?> getEventAnnotationClass();

  /**
   * Process the given event. There's no integrity checks at this point, the user should carefully
   * manage when to fire the trigger using the {@link #isDefinedAt(MovilizerFilterParams)} method
   * and {@link #getEventClass()} prior the call.
   * 
   * @param event to be processed.
   */
  void fire(MovilizerEvent event);

  /**
   * Priority order in which the trigger will be fired among its peers (triggers with the same event
   * class as target). Maximum priority will be 0 and minimum priority will be -1. E.g.:
   * T1(priority=2), T2(priority=-1), T3(priority=0) => order of execution T3, T1, T2.
   *
   * @return priority for the firing order.
   */
  Integer getPriority();

  /**
   * {@link MovilizerFilterParams} used in the definition of the trigger. This filter will be the
   * one to be used for comparison in {@link #isDefinedAt(MovilizerFilterParams)}.
   *
   * @return filter definition of the trigger.
   */
  MovilizerFilterParams getFilter();

  /**
   * Compares event filter params with the actual trigger filter to see if the filter needs to be
   * fired.
   *
   * @param filterParams the event filter params to be compared with the triggers filter.
   * @return true in case the trigger needs to be fired.
   */
  Boolean isDefinedAt(MovilizerFilterParams filterParams);
}
