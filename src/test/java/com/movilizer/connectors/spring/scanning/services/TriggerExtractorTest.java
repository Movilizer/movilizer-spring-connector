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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.movilizer.connectors.spring.annotations.triggers.OnNewDataContainers;
import com.movilizer.connectors.spring.annotations.triggers.OnNewReplies;
import com.movilizer.connectors.spring.annotations.triggers.OnNewReply;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.MovilizerFilterParams;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;
import com.movilizer.connectors.spring.scanning.services.extractors.TriggerAnnotationExtractor;
import com.movilizer.connectors.spring.testdata.extra.extractors.newdatacontainers.NewDataContainersTriggers;
import com.movilizer.connectors.spring.testdata.extra.extractors.newreplies.NewRepliesTriggers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(JUnit4.class)
public class TriggerExtractorTest {

  ClassFinder classFinder;
  Class<?> movilizerComponents1 = NewRepliesTriggers.class;
  Method method11;
  Method method12;
  Class<?> movilizerComponents2 = NewDataContainersTriggers.class;
  Method method21;
  Method method22;
  List<TriggerAnnotationExtractor> triggerExtractors;
  TriggerAnnotationExtractor triggerAnnotationExtractor1;
  Class trigger1Annotation = OnNewReplies.class;
  TriggerAnnotationExtractor triggerAnnotationExtractor2;
  Class trigger2Annotation = OnNewDataContainers.class;

  List<MovilizerAppEndpoint> endpoints;
  MovilizerAppEndpoint endpoint1;
  String endpoint1Name = "endpoint1";
  MovilizerAppEndpoint endpoint2;
  String endpoint2Name = "endpoint2";

  MovilizerTrigger trigger11;
  String trigger11EndpointsPattern = "endpoint1|endpoint2";
  MovilizerTrigger trigger12;
  String trigger12EndpointsPattern = "endpoint1";
  MovilizerTrigger trigger21;
  String trigger21EndpointsPattern = "endpoint1|endpoint2";
  MovilizerTrigger trigger22;
  String trigger22EndpointsPattern = "endpoint2";

  TriggerExtractor extractor;

  @Before
  public void setUp() throws Exception {
    // Init test vars
    triggerExtractors = new ArrayList<>();
    endpoints = new ArrayList<>();
    Set<Class<?>> movilizerComponents = new HashSet<>();
    movilizerComponents.add(movilizerComponents1);
    movilizerComponents.add(movilizerComponents2);

    method11 =
        movilizerComponents1.getDeclaredMethod("processAllNewReplies", List.class, List.class,
            Long.class, String.class, Calendar.class, Calendar.class);
    assertThat(method11, is(notNullValue()));
    method12 = movilizerComponents1.getDeclaredMethod("processFromSystemId", List.class);
    assertThat(method12, is(notNullValue()));
    method21 =
        movilizerComponents2.getDeclaredMethod("processAllNewDataContainers", List.class,
            List.class, Long.class, String.class, Calendar.class, Calendar.class, Short.class,
            String.class, String.class, String.class, Long.class, String.class, String.class);
    assertThat(method21, is(notNullValue()));
    method22 = movilizerComponents2.getDeclaredMethod("processFromSystemId", List.class);
    assertThat(method22, is(notNullValue()));

    // Setup mocks
    classFinder = mock(ClassFinder.class);
    when(classFinder.findClassesAnnotatedWith(any(Class.class), any(String.class))).thenReturn(
        movilizerComponents);

    endpoint1 = mock(MovilizerAppEndpoint.class);
    when(endpoint1.getName()).thenReturn(endpoint1Name);
    endpoints.add(endpoint1);

    endpoint2 = mock(MovilizerAppEndpoint.class);
    when(endpoint2.getName()).thenReturn(endpoint2Name);
    endpoints.add(endpoint2);

    trigger11 = mock(MovilizerTrigger.class);
    MovilizerFilterParams filter1 = mock(MovilizerFilterParams.class);
    when(filter1.getEndpoint()).thenReturn(trigger11EndpointsPattern);
    when(trigger11.getFilter()).thenReturn(filter1);

    trigger12 = mock(MovilizerTrigger.class);
    MovilizerFilterParams filter2 = mock(MovilizerFilterParams.class);
    when(filter2.getEndpoint()).thenReturn(trigger12EndpointsPattern);
    when(trigger12.getFilter()).thenReturn(filter2);

    trigger21 = mock(MovilizerTrigger.class);
    MovilizerFilterParams filter3 = mock(MovilizerFilterParams.class);
    when(filter3.getEndpoint()).thenReturn(trigger21EndpointsPattern);
    when(trigger21.getFilter()).thenReturn(filter3);

    trigger22 = mock(MovilizerTrigger.class);
    MovilizerFilterParams filter4 = mock(MovilizerFilterParams.class);
    when(filter4.getEndpoint()).thenReturn(trigger22EndpointsPattern);
    when(trigger22.getFilter()).thenReturn(filter4);

    triggerAnnotationExtractor1 = mock(TriggerAnnotationExtractor.class);
    doReturn(trigger1Annotation).when(triggerAnnotationExtractor1).getAnnotation();
    when(
        triggerAnnotationExtractor1.getTrigger(eq(method11), anyListOf(MovilizerAppEndpoint.class)))
        .thenReturn(trigger11);
    when(
        triggerAnnotationExtractor1.getTrigger(eq(method12), anyListOf(MovilizerAppEndpoint.class)))
        .thenReturn(trigger12);
    triggerExtractors.add(triggerAnnotationExtractor1);

    triggerAnnotationExtractor2 = mock(TriggerAnnotationExtractor.class);
    doReturn(trigger2Annotation).when(triggerAnnotationExtractor2).getAnnotation();
    when(
        triggerAnnotationExtractor2.getTrigger(eq(method21), anyListOf(MovilizerAppEndpoint.class)))
        .thenReturn(trigger21);
    when(
        triggerAnnotationExtractor2.getTrigger(eq(method22), anyListOf(MovilizerAppEndpoint.class)))
        .thenReturn(trigger22);

    triggerExtractors.add(triggerAnnotationExtractor2);

    extractor = new TriggerExtractor(classFinder, triggerExtractors);
  }

  @Test
  public void testConnectTriggersToEndpoints() throws Exception {
    Map<Class<?>, List<MovilizerTrigger>> classTriggersMap = new HashMap<>();
    List<MovilizerTrigger> triggers1 = new ArrayList<>();
    triggers1.add(trigger11);
    triggers1.add(trigger12);
    List<MovilizerTrigger> triggers2 = new ArrayList<>();
    triggers2.add(trigger21);
    triggers2.add(trigger22);
    classTriggersMap.put(movilizerComponents1, triggers1);
    classTriggersMap.put(movilizerComponents2, triggers2);

    Map<MovilizerAppEndpoint, List<MovilizerTrigger>> result =
        extractor.connectTriggersToEndpoints(endpoints, classTriggersMap);
    assertThat(result, is(notNullValue()));
    assertThat(result, hasKey(endpoint1));
    assertThat(result.get(endpoint1).size(), is(3));
    assertThat(result.get(endpoint1), hasItem(trigger11));
    assertThat(result.get(endpoint1), hasItem(trigger12));
    assertThat(result.get(endpoint1), hasItem(trigger21));

    assertThat(result, hasKey(endpoint2));
    assertThat(result.get(endpoint2).size(), is(3));
    assertThat(result.get(endpoint2), hasItem(trigger11));
    assertThat(result.get(endpoint2), hasItem(trigger21));
    assertThat(result.get(endpoint2), hasItem(trigger22));
  }

  @Test
  public void testOneOfTrue() throws Exception {
    List<Annotation> fromMethod = Arrays.asList(method11.getDeclaredAnnotations());
    List<Class<? extends Annotation>> anyOfThese =
        Arrays.asList(OnNewDataContainers.class, OnNewReplies.class);
    Boolean result = extractor.oneOf(fromMethod, anyOfThese);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(true));
  }

  @Test
  public void testOneOfFalse() throws Exception {
    List<Annotation> fromMethod = Arrays.asList(method11.getDeclaredAnnotations());
    List<Class<? extends Annotation>> anyOfThese = new ArrayList<>();
    anyOfThese.add(OnNewReply.class);
    Boolean result = extractor.oneOf(fromMethod, anyOfThese);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }

  @Test
  public void testOneOfEmptyMethodAnnotationList() throws Exception {
    List<Annotation> fromMethod = new ArrayList<>();
    List<Class<? extends Annotation>> anyOfThese =
        Arrays.asList(OnNewDataContainers.class, OnNewReplies.class);
    Boolean result = extractor.oneOf(fromMethod, anyOfThese);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }

  @Test
  public void testOneOfEmptyAvailableAnnotationList() throws Exception {
    List<Annotation> fromMethod = Arrays.asList(method11.getDeclaredAnnotations());
    List<Class<? extends Annotation>> anyOfThese = new ArrayList<>();
    Boolean result = extractor.oneOf(fromMethod, anyOfThese);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }

  @Test
  public void testExtractTriggersFromClass() throws Exception {
    List<MovilizerTrigger> result =
        extractor.extractTriggersFromClass(movilizerComponents1, endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result.size(), is(2));
    assertThat(result, hasItem(trigger11));
    assertThat(result, hasItem(trigger12));
  }

  @Test
  public void testGetTriggersForBasePackage() throws Exception {
    Map<Class<?>, List<MovilizerTrigger>> result =
        extractor.getTriggersForBasePackage("mocked-not-needed", endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result.size(), is(2));
    assertThat(result.containsKey(movilizerComponents1), is(true));
    assertThat(result.get(movilizerComponents1), is(notNullValue()));
    assertThat(result.get(movilizerComponents1).size(), is(2));
    assertThat(result.get(movilizerComponents1), hasItem(trigger11));
    assertThat(result.get(movilizerComponents1), hasItem(trigger12));
    assertThat(result.containsKey(movilizerComponents2), is(true));
    assertThat(result.get(movilizerComponents2), is(notNullValue()));
    assertThat(result.get(movilizerComponents2).size(), is(2));
    assertThat(result.get(movilizerComponents2), hasItem(trigger21));
    assertThat(result.get(movilizerComponents2), hasItem(trigger22));
  }
}
