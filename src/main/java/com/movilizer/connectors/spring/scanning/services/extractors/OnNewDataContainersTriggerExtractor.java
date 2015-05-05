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


import com.movilizer.connectors.spring.annotations.triggers.OnNewDataContainers;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.exceptions.IllegalTriggerDefinitionException;
import com.movilizer.connectors.spring.model.impl.TriggerFilterParams;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;
import com.movilizer.connectors.spring.model.triggers.NewDataContainerTrigger;
import com.movilizer.connectors.spring.utils.StringUtils;
import com.movilizer.connectors.spring.utils.TriggerExtractorUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Tool to extract the triggers from the annotation {@link OnNewDataContainers}.
 *
 * @author Jesús de Mula Cano
 * @see OnNewDataContainers
 * @see NewDataContainerTrigger
 * @since 0.1
 */
@Component
public class OnNewDataContainersTriggerExtractor implements TriggerAnnotationExtractor {
  private static Log logger = LogFactory.getLog(OnNewDataContainersTriggerExtractor.class);

  @Override
  public MovilizerTrigger getTrigger(Method method, List<MovilizerAppEndpoint> endpoints)
      throws IllegalAccessException, NoSuchMethodException {
    OnNewDataContainers onNewDataContainers = method.getAnnotation(OnNewDataContainers.class);
    NewDataContainerTrigger trigger = null;
    if (onNewDataContainers != null) {
      String endpointPattern = onNewDataContainers.endpoint();
      String systemIdPattern = onNewDataContainers.systemId();
      try {
        checkEndpointNamePattern(endpointPattern, endpoints, method);
        checkSystemIdPattern(systemIdPattern, endpoints, method);
        TriggerFilterParams filter = createTriggerFilter(onNewDataContainers, endpoints);
        trigger = new NewDataContainerTrigger(onNewDataContainers.priority(), filter, method);
      } catch (PatternSyntaxException e) {
        String msg =
            String.format("Wrong regex pattern '%s' for trigger '%s' in %s#%s", e.getPattern(),
                OnNewDataContainers.class.getName(), method.getClass().getName(), method.getName());
        if (logger.isErrorEnabled()) {
          logger.error(msg, e);
        }
        throw new IllegalTriggerDefinitionException(msg, e);
      }

    }
    return trigger;
  }

  private void checkEndpointNamePattern(String endpointPattern,
      List<MovilizerAppEndpoint> endpoints, Method method) throws IllegalTriggerDefinitionException {
    if (!TriggerExtractorUtils.isEndpointPatternValid(endpointPattern, endpoints)) {
      String validEndpointNames =
          StringUtils.collectionToCommaDelimitedString(TriggerExtractorUtils
              .validEndpointNames(endpoints));
      String msg =
          String.format("Invalid endpoint name pattern '%s' found in class %s. Accepted are: %s",
              endpointPattern, method.getDeclaringClass().getName(), validEndpointNames);
      if (logger.isErrorEnabled()) {
        logger.error(msg);
      }
      throw new IllegalTriggerDefinitionException(msg);
    }
  }


  private void checkSystemIdPattern(String systemIdPattern, List<MovilizerAppEndpoint> endpoints,
      Method method) throws IllegalTriggerDefinitionException {
    if (!TriggerExtractorUtils.isSystemIdPatternValid(systemIdPattern, endpoints)) {
      String validSystemIds =
          StringUtils.collectionToCommaDelimitedString(TriggerExtractorUtils
              .validSystemIds(endpoints));
      String msg =
          String.format("Invalid endpoint name pattern '%s' found in class %s. Accepted are: %s",
              systemIdPattern, method.getDeclaringClass().getName(), validSystemIds);
      if (logger.isErrorEnabled()) {
        logger.error(msg);
      }
      throw new IllegalTriggerDefinitionException(msg);
    }
  }

  private TriggerFilterParams createTriggerFilter(OnNewDataContainers onNewDataContainers,
      List<MovilizerAppEndpoint> endpoints) {
    TriggerFilterParams filter =
        new TriggerFilterParams(onNewDataContainers.endpoint(), onNewDataContainers.systemId(),
            onNewDataContainers.responseQueue(), onNewDataContainers.containerUploadPriority(),
            onNewDataContainers.key(), onNewDataContainers.moveletKey(),
            onNewDataContainers.moveletKeyExtension(), onNewDataContainers.moveletVersion(),
            onNewDataContainers.participantKey(), onNewDataContainers.deviceAddress());
    filter.applyDefaults(endpoints);
    return filter;
  }

  @Override
  public Class<? extends Annotation> getAnnotation() {
    return OnNewDataContainers.class;
  }
}
