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

package com.movilizer.connectors.spring.scanning.services.extractors;

import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Tool to extract triggers knowing the underlining annotation where the information needed to build
 * the trigger comes from.
 *
 * @author Jesús de Mula Cano
 * @see com.movilizer.connectors.spring.annotations.triggers.MovilizerTrigger
 * @see MovilizerTrigger
 * @since 0.1
 */
public interface TriggerAnnotationExtractor {

  /**
   * Extract a {@link MovilizerTrigger} from the method given taking into account the endpoints in
   * scope.
   *
   * @param method containing a
   *        {@link com.movilizer.connectors.spring.annotations.triggers.MovilizerTrigger}
   *        annotation.
   * @param endpoints in scope.
   * @return a trigger instance if the method contains the {@link #getAnnotation()}, {@code null} if
   *         not.
   * @throws IllegalAccessException when the method is not accessible.
   * @throws NoSuchMethodException when the method or annotation doesn't exist.
   */
  MovilizerTrigger getTrigger(Method method, List<MovilizerAppEndpoint> endpoints)
      throws IllegalAccessException, NoSuchMethodException;

  /**
   * Trigger annotation from the
   * {@link com.movilizer.connectors.spring.annotations.triggers.MovilizerTrigger} package.
   * 
   * @return annotation that the trigger extractor works on.
   */
  Class<? extends Annotation> getAnnotation();
}
