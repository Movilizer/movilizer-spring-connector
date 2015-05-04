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

package com.movilizer.connectors.spring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the packages to be scanned for {@link MovilizerComponent}.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerComponent
 * @since 0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MovilizerComponentScan {

  /**
   * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
   * declarations e.g.: {@code @MovilizerComponentScan("org.my.pkg")} instead of
   * {@code @MovilizerComponentScan(basePackages="org.my.pkg")}.
   */
  String[] value() default {};

  /**
   * Base packages to scan for annotated components.
   * <p/>
   * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
   * <p/>
   * Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
   */
  String[] basePackages() default {};

  /**
   * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for
   * annotated components. The package of each class specified will be scanned.
   * <p/>
   * Consider creating a special no-op marker class or interface in each package that serves no
   * purpose other than being referenced by this attribute.
   */
  Class<?>[] basePackageClasses() default {};
}
