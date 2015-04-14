/*
 * Copyright 2015 Movilizer GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movilizer.connectors.spring.annotations;

import java.lang.annotation.*;

/**
 * Main entry point that defines a Movilizer app. If no other base scan package is specified the
 * base package for looking MovilizerComponents will be the same as the one containing this
 * annotation.
 * <p/>
 * Parameters available:
 * <ul>
 * <li>name - the app name</li>
 * <li>version - the version of the app. e.g.: demo or production (default: "")</li>
 * </ul>
 * <p/>
 * The only required parameter is the name of the app, all others have sensible defaults. If no {@link MovilizerConfig}
 * is present the default values will be tried in the following order in search of properties:
 * <ul>
 * <li>classpath:${name}-${version}.yml (if version is "" the dash will be omitted)</li>
 * <li>classpath:${name}-${version}.properties (if version is "" the dash will be omitted)</li>
 * <li>classpath:${name}.yml</li>
 * <li>classpath:${name}.properties</li>
 * <li>classpath:${currentPackage}/${name}-${version}.yml (if version is "" the dash will be omitted)</li>
 * <li>classpath:${currentPackage}/${name}-${version}.properties (if version is "" the dash will be omitted)</li>
 * <li>classpath:${currentPackage}/${name}.yml</li>
 * <li>classpath:${currentPackage}/${name}.properties</li>
 * </ul>
 * <p/>
 * Important: This attribute will be included as part of the movelet as extension key overriding any extension key that
 * the movelets might have.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerComponent
 * @since 0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MovilizerApp {

    /**
     * Alias for the {@link #name()} attribute.
     * <p/>
     * Allows for more concise annotation declarations e.g.:
     * {@code @MovilizerApp("superSecretApp")} instead of
     * {@code @MovilizerApp(name="superSecretApp")}.
     */
    String value();

    /**
     * Name that identifies the app. In addition with the {@link #version()} it should be unique in the entire context.
     */
    String name() default "";

    /**
     * Version of the app meant for running different environments of the same app at the same time.
     * <p/>
     * Important: This attribute will be included as part of the movelet as extension key overriding any extension key
     * that the movelets might have.
     */
    String version() default "";
}
