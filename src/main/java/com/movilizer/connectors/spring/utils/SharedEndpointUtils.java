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

import com.movilizer.connectors.spring.model.MovilizerAppContext;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.MovilizerContext;
import com.movilizer.connectors.spring.model.MovilizerSharedEndpoint;
import com.movilizer.connectors.spring.model.impl.MovilizerSharedEndpointImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tools for working with Movilizer shared endpoints.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public abstract class SharedEndpointUtils {

  public static List<MovilizerSharedEndpoint> extractSharedEndpointFromContext(
      MovilizerContext context) {
    Map<Object, MovilizerSharedEndpoint> acc = new HashMap<>();
    for (MovilizerAppContext app : context.getApps()) {
      for (MovilizerAppEndpoint endpoint : app.getEndpoints()) {
        MovilizerSharedEndpoint iterSharedEndpoint = new MovilizerSharedEndpointImpl(app, endpoint);
        Object sharedEndpointKey = iterSharedEndpoint.consistentHashKey();
        if (!acc.containsKey(sharedEndpointKey)) {
          acc.put(sharedEndpointKey, iterSharedEndpoint);
        } else {
          acc.get(sharedEndpointKey).addApp(app);
        }
      }
    }
    return new ArrayList<>(acc.values());
  }
}
