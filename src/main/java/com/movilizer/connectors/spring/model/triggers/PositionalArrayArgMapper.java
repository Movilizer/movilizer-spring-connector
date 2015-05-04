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

import com.movilizer.connectors.spring.utils.ClassUtils;

import java.lang.reflect.Method;

/**
 * Positional argument mapper that maps the source parameters to the target parameters for those
 * parameter that have the same name and class.
 *
 * @author Jesús de Mula Cano
 * @see ArgMapper
 * @since 0.1
 */
public class PositionalArrayArgMapper implements ArgMapper {
  protected static final int NO_POSITION_FOUND = -1;
  private int[] targetToSourcePositions;

  /**
   * Creates a new instance for the mapper by reflection of both methods: target and source.
   * 
   * @param sourceMethod method were the arguments are known beforehand.
   * @param targetMethod method which signature is to be matched.
   */
  public PositionalArrayArgMapper(Method sourceMethod, Method targetMethod) {
    this.targetToSourcePositions = positionsMapping(sourceMethod, targetMethod);
  }

  protected int[] positionsMapping(Method sourceMethod, Method targetMethod) {
    String[] targetParams = ClassUtils.getParameterNames(targetMethod);
    Class<?>[] targetParamClasses = targetMethod.getParameterTypes();
    String[] sourceParams = ClassUtils.getParameterNames(sourceMethod);
    Class<?>[] sourceParamClasses = sourceMethod.getParameterTypes();
    int[] targetToSourcePositions = new int[targetParamClasses.length];
    for (int i = 0; i < targetParamClasses.length; i++) {
      int sourcePosition =
          findByNameAndClass(targetParams[i], targetParamClasses[i], sourceParams,
              sourceParamClasses);
      if (sourcePosition > NO_POSITION_FOUND) {
        targetToSourcePositions[i] = sourcePosition;
      } else {
        targetToSourcePositions[i] = NO_POSITION_FOUND;
      }
    }
    return targetToSourcePositions;
  }

  protected int findByNameAndClass(String param, Class<?> paramClass, String[] sourceParams,
      Class<?>[] sourceParamClasses) {
    for (int i = 0; i < sourceParams.length; i++) {
      if (param.equals(sourceParams[i]) && paramClass.equals(sourceParamClasses[i])) {
        return i;
      }
    }
    return NO_POSITION_FOUND;
  }

  @Override
  public Object[] map(Object... sourceMethodArgs) {
    Object[] targetMethodArgs = new Object[targetToSourcePositions.length];
    for (int i = 0; i < targetToSourcePositions.length; i++) {
      if (targetToSourcePositions[i] > NO_POSITION_FOUND) {
        targetMethodArgs[i] = sourceMethodArgs[targetToSourcePositions[i]];
      } else {
        targetMethodArgs[i] = null;
      }
    }
    return targetMethodArgs;
  }
}
