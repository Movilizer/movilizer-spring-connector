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
 * Identifies the config files to be used for the specific {@link MovilizerApp}. It accepts .properties and .yml file
 * types. If multiple files are present they will be consolidated in the same context.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MovilizerConfig {

    /**
     * Path to the actual file containing the properties e.g.: "myAppFolder/movilizerApp.yml".
     */
    String[] value();
}
