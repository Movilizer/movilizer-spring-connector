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

package com.movilizer.connectors.spring.scanning.services;

import com.movilizer.connectors.spring.annotations.MovilizerComponent;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.exceptions.IllegalTriggerDefinitionException;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;
import com.movilizer.connectors.spring.scanning.services.extractors.TriggerAnnotationExtractor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service to find a extract the trigger information from a given Movilzer App context.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerComponent
 * @since 0.1
 */
@Component
public class TriggerExtractor {
  private static Log logger = LogFactory.getLog(TriggerExtractor.class);
  private final List<Class<? extends Annotation>> movilizerTriggerAnnotations;
  private final ClassFinder classFinder;
  private final List<TriggerAnnotationExtractor> triggerExtractors;

  /**
   * Create a {@link TriggerExtractor} given a class finder service.
   * 
   * @param classFinder to use for finding the {@link MovilizerComponent} classes.
   */
  @Autowired
  public TriggerExtractor(ClassFinder classFinder,
      List<TriggerAnnotationExtractor> triggerExtractors) {
    this.classFinder = classFinder;
    this.triggerExtractors = triggerExtractors;
    movilizerTriggerAnnotations = new ArrayList<>();
    for (TriggerAnnotationExtractor triggerExtractor : triggerExtractors) {
      movilizerTriggerAnnotations.add(triggerExtractor.getAnnotation());
    }
  }

  /**
   * Finds all triggers in the base packages given and defaults the values needed to the endpoints
   * of the app.
   *
   * @param endpoints to use for defaults.
   * @param basePackages to scan for {@link MovilizerComponent}
   * @return a map with the triggers of each endpoint.
   */
  public Map<MovilizerAppEndpoint, List<MovilizerTrigger>> getAppTriggers(
      List<MovilizerAppEndpoint> endpoints, Set<String> basePackages) {
    Map<Class<?>, List<MovilizerTrigger>> classTriggers = new HashMap<>();
    for (String basePackage : basePackages) {
      classTriggers.putAll(getTriggersForBasePackage(basePackage, endpoints));
    }
    return connectTriggersToEndpoints(endpoints, classTriggers);
  }

  protected Map<Class<?>, List<MovilizerTrigger>> getTriggersForBasePackage(String basePackage,
      List<MovilizerAppEndpoint> endpoints) {
    Set<Class<?>> movilizerComponents =
        classFinder.findClassesAnnotatedWith(MovilizerComponent.class, basePackage);
    Map<Class<?>, List<MovilizerTrigger>> classTriggers = new HashMap<>();
    for (Class<?> movilizerComponent : movilizerComponents) {
      classTriggers
          .put(movilizerComponent, extractTriggersFromClass(movilizerComponent, endpoints));
    }
    return classTriggers;
  }

  protected List<MovilizerTrigger> extractTriggersFromClass(Class<?> movilizerComponent,
      List<MovilizerAppEndpoint> endpoints) {
    List<MovilizerTrigger> triggers = new ArrayList<>();
    for (Method method : movilizerComponent.getDeclaredMethods()) {
      List<Annotation> methodAnnotations = Arrays.asList(method.getDeclaredAnnotations());
      if (oneOf(methodAnnotations, movilizerTriggerAnnotations)) {
        for (TriggerAnnotationExtractor triggerExtractor : triggerExtractors) {
          try {
            MovilizerTrigger trigger = triggerExtractor.getTrigger(method, endpoints);
            if (trigger != null) {
              triggers.add(trigger);
            }
          } catch (IllegalTriggerDefinitionException | IllegalAccessException
              | NoSuchMethodException e) {
            if (logger.isWarnEnabled()) {
              logger.warn(String.format("Error while loading trigger '%s' of class %s",
                  method.getName(), method.getDeclaringClass().getCanonicalName()), e);
            }
          }
        }
      }
    }
    return triggers;
  }

  protected Boolean oneOf(List<Annotation> fromMethod, List<Class<? extends Annotation>> anyOfThese) {
    for (Annotation annotation : fromMethod) {
      if (anyOfThese.contains(annotation.annotationType())) {
        return true;
      }
    }
    return false;
  }

  protected Map<MovilizerAppEndpoint, List<MovilizerTrigger>> connectTriggersToEndpoints(
      List<MovilizerAppEndpoint> endpoints, Map<Class<?>, List<MovilizerTrigger>> classTriggersMap) {

    Map<MovilizerAppEndpoint, List<MovilizerTrigger>> connectedTriggers = new HashMap<>();
    if (!endpoints.isEmpty()) {
      for (List<MovilizerTrigger> movilizerTriggersInClass : classTriggersMap.values()) {
        for (MovilizerTrigger trigger : movilizerTriggersInClass) {
          String endpointsPattern = trigger.getFilter().getEndpoint();
          for (MovilizerAppEndpoint endpoint : endpoints) {
            if (endpointsPattern.contains(endpoint.getName())) {
              addTriggerToEndpointInConnectedTriggers(connectedTriggers, endpoint, trigger);
            }
          }
        }
      }
    }
    return connectedTriggers;
  }

  private void addTriggerToEndpointInConnectedTriggers(
      Map<MovilizerAppEndpoint, List<MovilizerTrigger>> connectedTriggers,
      MovilizerAppEndpoint endpoint, MovilizerTrigger trigger) {
    if (connectedTriggers.containsKey(endpoint)) {
      connectedTriggers.get(endpoint).add(trigger);
    } else {
      List<MovilizerTrigger> newTriggerList = new ArrayList<>();
      newTriggerList.add(trigger);
      connectedTriggers.put(endpoint, newTriggerList);
    }
  }
}
