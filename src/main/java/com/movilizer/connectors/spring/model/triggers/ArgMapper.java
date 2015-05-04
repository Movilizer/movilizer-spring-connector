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

/**
 * Map the arguments given to an array matching the signature of the target method containing an
 * event annotation in the package of
 * {@link com.movilizer.connectors.spring.annotations.triggers.MovilizerTrigger}
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public interface ArgMapper {

  /**
   * Given the source arguments of a method coming from an implementation of
   * {@link MovilizerTrigger} return the array with the arguments of the method annotated with a
   * {@link com.movilizer.connectors.spring.annotations.triggers.MovilizerTrigger}.
   *
   * @param sourceMethodArgs to map.
   * @return targetMethodArgs to do a reflection call.
   */
  Object[] map(Object... sourceMethodArgs);
}
