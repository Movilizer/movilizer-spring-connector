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

package com.movilizer.connectors.spring.scanning;


import com.movilizer.connectors.spring.model.MovilizerAppContext;

import java.util.List;


/**
 * Class used to retrieved all the Movilizer apps that are to be run by the connectors.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerAppContext
 * @since 0.1
 */
public interface AppScanner {

  /**
   * Retrieves all the apps to be used by the connector.
   *
   * @return all the apps available to run.
   */
  List<MovilizerAppContext> getApps();
}
