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

package com.movilizer.connectors.spring.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.PrioritizedParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Shared tools for working with classes across the connector.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public abstract class ClassUtils {

  private static final PrioritizedParameterNameDiscoverer parameterNameDiscoverer =
      new DefaultParameterNameDiscoverer();

  /**
   * From {@code org.springframework.util.ClassUtils#getMethodIfAvailable}.
   * <p/>
   * Determine whether the given class has a public method with the given signature, and return it
   * if available (else return {@code null}).
   * <p/>
   * In case of any signature specified, only returns the method if there is a unique candidate,
   * i.e. a single public method with the specified name.
   * <p/>
   * Essentially translates {@code NoSuchMethodException} to {@code null}.
   * 
   * @param clazz the clazz to analyze
   * @param methodName the name of the method
   * @param paramTypes the parameter types of the method (may be {@code null} to indicate any
   *        signature)
   * @return the method, or {@code null} if not found
   * @see Class#getMethod
   */
  public static Method getMethodIfAvailable(Class<?> clazz, String methodName,
      Class<?>... paramTypes) {
    return org.springframework.util.ClassUtils.getMethodIfAvailable(clazz, methodName, paramTypes);
  }

  /**
   * Get the method parameter names. From
   * {@link PrioritizedParameterNameDiscoverer#getParameterNames(Method)}
   *
   * @param method to inspect.
   * @return the array of names of the parameters.
   * @see PrioritizedParameterNameDiscoverer#getParameterNames
   */
  public static String[] getParameterNames(Method method) {
    return parameterNameDiscoverer.getParameterNames(method);
  }

  /**
   * Get the method parameter classes.
   *
   * @param method to inspect.
   * @return array of the classes of the method parameters.
   * @see Method#getParameterTypes
   */
  public static Class<?>[] getParameterTypes(Method method) {
    return method.getParameterTypes();
  }

  /**
   * From {@code org.springframework.util.ClassUtils#isAssignableValue(Class,Class)}.
   * <p/>
   * Check if the right-hand side type may be assigned to the left-hand side type, assuming setting
   * by reflection. Considers primitive wrapper classes as assignable to the corresponding primitive
   * types.
   *
   * @param lhsType the target type
   * @param rhsType the value type that should be assigned to the target type
   * @return if the target type is assignable from the value type
   */
  public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
    return org.springframework.util.ClassUtils.isAssignableValue(lhsType, rhsType);
  }

  /**
   * From {@code org.springframework.util.ClassUtils#isAssignableValue(Class,Object)}.
   * <p/>
   * Determine if the given type is assignable from the given value, assuming setting by reflection.
   * Considers primitive wrapper classes as assignable to the corresponding primitive types.
   *
   * @param type the target type
   * @param value the value that should be assigned to the type
   * @return if the type is assignable from the value
   */
  public static boolean isAssignableValue(Class<?> type, Object value) {
    return org.springframework.util.ClassUtils.isAssignableValue(type, value);
  }

  /**
   * Make a method accessible.
   *
   * @param method to make accessible.
   */
  public static void makeAccessible(Method method) {
    if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass()
        .getModifiers())) && !method.isAccessible()) {
      method.setAccessible(true);
    }
  }

  /**
   * Invoke a method given a instnce of the class and the parameter of the method.
   *
   * @param method to be invoked.
   * @param target instance on which the function will be applied on.
   * @param args arguments of the method to be invoked.
   * @return return value of the method.
   */
  public static Object invokeMethod(Method method, Object target, Object... args) {
    return org.springframework.util.ReflectionUtils.invokeMethod(method, target, args);
  }
}
