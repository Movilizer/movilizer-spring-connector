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

package com.movilizer.connectors.spring.annotations.triggers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the method to be run when new datacontainers comes into the system.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnNewDataContainers {

  /**
   * Priority in which the trigger will be executed among its peers in the app. Max priority = 0, No
   * priority = -1 (default).
   */
  int priority() default -1;

  /**
   * Indicates the endpoint names to which this trigger applies. If empty the connector will
   * auto-fill with all the endpoints defined in the app.
   */
  String endpoint() default "";

  /**
   * Indicates the system ids to which this trigger applies. If empty the connector will auto-fill
   * with all the system ids defined in the app.
   */
  String systemId() default "";

  /**
   * Indicates the response queues to which this trigger applies (Default is the default queue).
   */
  String responseQueue() default "";

  /**
   * Indicates the container upload priority to which this trigger applies (Default is none).
   */
  String containerUploadPriority() default "";

  /**
   * Indicates the container key to which this trigger applies (Default is none).
   */
  String key() default "";

  /**
   * Indicates the movelet key to which this trigger applies (Default is none).
   */
  String moveletKey() default "";

  /**
   * Indicates the movelet extension key to which this trigger applies (Default is none).
   */
  String moveletKeyExtension() default "";

  /**
   * Indicates the movelet version to which this trigger applies (Default is none).
   */
  String moveletVersion() default "";

  /**
   * Indicates the participant key to which this trigger applies (Default is none).
   */
  String participantKey() default "";

  /**
   * Indicates the participant device address to which this trigger applies (Default is none).
   */
  String deviceAddress() default "";

  /**
   * Indicates the mapper to use to convert from {@code MovilizerUploadDataContainer} to one of the
   * trigger method parameters (Default is Object.class).
   */
  Class<?> mapper() default Object.class;
}
