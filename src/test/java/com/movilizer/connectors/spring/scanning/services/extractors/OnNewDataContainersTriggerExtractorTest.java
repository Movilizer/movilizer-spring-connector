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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.spy;

import com.movilizer.connectors.spring.annotations.triggers.OnNewDataContainers;
import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.MovilizerFilterParams;
import com.movilizer.connectors.spring.model.exceptions.IllegalTriggerDefinitionException;
import com.movilizer.connectors.spring.model.impl.MovilizerAppEndpointImpl;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class OnNewDataContainersTriggerExtractorTest {
  private static final Logger logger = LoggerFactory
      .getLogger(OnNewDataContainersTriggerExtractorTest.class);
  private static final String endpointName1 = "endpoint-1";
  private static final String endpointSystemId1 = "1234";
  private static final int triggerPriority = 0;
  private static final String responseQueue = "1";
  private static final String containerUploadPriority = "2";
  private static final String datacontainerKey = "dckey";
  private static final String moveletKey = "mkey";
  private static final String moveletKeyExtension = "mekey";
  private static final String moveletVersion = "mversion";
  private static final String participantKey = "pkey";
  private static final String deviceAddress = "da";
  private static final String endpointName2 = "endpoint-2";
  private static final String endpointSystemId2 = "4321";

  List<MovilizerAppEndpoint> endpoints = null;
  Object bean = null;
  Method mostOpenTriggerMethod = null;
  Method mostRestrictiveTriggerMethod = null;
  Method invalidTriggerMethod = null;
  Method badRegexTriggerMethod = null;
  Method nonAnnonMethod = null;
  OnNewDataContainersTriggerExtractor extractor = null;

  @Before
  public void setUp() throws Exception {
    extractor = new OnNewDataContainersTriggerExtractor();
    endpoints = new ArrayList<>();
    MovilizerAppEndpoint endpoint1 =
        new MovilizerAppEndpointImpl(endpointName1, Long.parseLong(endpointSystemId1), "", "", "",
            "", 60L, 0, 0);
    endpoints.add(endpoint1);
    MovilizerAppEndpoint endpoint2 =
        new MovilizerAppEndpointImpl(endpointName2, Long.parseLong(endpointSystemId2), "", "", "",
            "", 60L, 0, 0);
    endpoints.add(endpoint2);

    TestTriggerClass myTestTriggerClass = new TestTriggerClass() {
      @OnNewDataContainers
      @Override
      public Boolean mostOpenTrigger(Long systemId, String nonRelevant) {
        logger.info(String.format(
            "triggerTargetMethod has been invoked. System id: %d, Non relevant: %s", systemId,
            nonRelevant));
        return true;
      }

      @OnNewDataContainers(priority = triggerPriority, endpoint = "endpoint-1",
          systemId = endpointSystemId1, responseQueue = responseQueue,
          containerUploadPriority = containerUploadPriority, key = datacontainerKey,
          moveletKey = moveletKey, moveletKeyExtension = moveletKeyExtension,
          moveletVersion = moveletVersion, participantKey = participantKey,
          deviceAddress = deviceAddress, mapper = TestTriggerClass.class)
      @Override
      public Boolean mostRestrictiveTrigger(Long systemId, String nonRelevant) {
        return null;
      }

      @OnNewDataContainers(systemId = "1337")
      @Override
      public Boolean invalidTrigger(Long systemId, String nonRelevant) {
        return null;
      }

      @OnNewDataContainers(endpoint = "[2-#*}")
      @Override
      public Boolean badRegexTrigger(Long systemId, String nonRelevant) {
        return null;
      }

      @Override
      public Boolean nonAnnonMethod(Long systemId, String nonRelevant) {
        return null;
      }
    };

    bean = myTestTriggerClass;
    mostOpenTriggerMethod =
        myTestTriggerClass.getClass()
            .getDeclaredMethod("mostOpenTrigger", Long.class, String.class);
    mostRestrictiveTriggerMethod =
        myTestTriggerClass.getClass().getDeclaredMethod("mostRestrictiveTrigger", Long.class,
            String.class);
    invalidTriggerMethod =
        myTestTriggerClass.getClass().getDeclaredMethod("invalidTrigger", Long.class, String.class);
    badRegexTriggerMethod =
        myTestTriggerClass.getClass()
            .getDeclaredMethod("badRegexTrigger", Long.class, String.class);
    nonAnnonMethod =
        myTestTriggerClass.getClass().getDeclaredMethod("nonAnnonMethod", Long.class, String.class);
  }

  @Test
  public void testGetTriggerWithMostOpenTriggerMethod() throws Exception {
    MovilizerTrigger trigger = extractor.getTrigger(mostOpenTriggerMethod, endpoints);
    assertThat(trigger, is(notNullValue()));
    assertThat(trigger.getPriority(), is(-1));
    MovilizerFilterParams triggerFilterParams = trigger.getFilter();
    assertThat(triggerFilterParams.getEndpoint(), containsString(endpointName1));
    assertThat(triggerFilterParams.getEndpoint(), containsString(endpointName2));
    assertThat(triggerFilterParams.getSystemId(), containsString(String.valueOf(endpointSystemId1)));
    assertThat(triggerFilterParams.getSystemId(), containsString(String.valueOf(endpointSystemId2)));
    assertThat(triggerFilterParams.getResponseQueue(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(triggerFilterParams.getContainerUploadPriority(),
        is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(triggerFilterParams.getKey(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(triggerFilterParams.getMoveletKey(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(triggerFilterParams.getMoveletKeyExtension(),
        is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(triggerFilterParams.getMoveletVersion(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(triggerFilterParams.getParticipantKey(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(triggerFilterParams.getDeviceAddress(), is(MovilizerFilterParams.WILDCARD_REGEX));

  }

  @Test
  public void testGetTriggerWithMostRestrictiveTriggerMethod() throws Exception {
    MovilizerTrigger trigger = extractor.getTrigger(mostRestrictiveTriggerMethod, endpoints);
    assertThat(trigger, is(notNullValue()));
    assertThat(trigger.getPriority(), is(triggerPriority));
    MovilizerFilterParams triggerFilterParams = trigger.getFilter();
    assertThat(triggerFilterParams.getEndpoint(), containsString(endpointName1));
    assertThat(triggerFilterParams.getSystemId(), containsString(endpointSystemId1));
    assertThat(triggerFilterParams.getResponseQueue(), is(responseQueue));
    assertThat(triggerFilterParams.getContainerUploadPriority(), is(containerUploadPriority));
    assertThat(triggerFilterParams.getKey(), is(datacontainerKey));
    assertThat(triggerFilterParams.getMoveletKey(), is(moveletKey));
    assertThat(triggerFilterParams.getMoveletKeyExtension(), is(moveletKeyExtension));
    assertThat(triggerFilterParams.getMoveletVersion(), is(moveletVersion));
    assertThat(triggerFilterParams.getParticipantKey(), is(participantKey));
    assertThat(triggerFilterParams.getDeviceAddress(), is(deviceAddress));
  }

  @Test(expected = IllegalTriggerDefinitionException.class)
  public void testGetTriggerWithInvalidTriggerMethod() throws Exception {
    OnNewDataContainersTriggerExtractor extractorTestProxy = spy(extractor);
    MovilizerTrigger trigger = extractorTestProxy.getTrigger(invalidTriggerMethod, endpoints);
  }

  @Test(expected = IllegalTriggerDefinitionException.class)
  public void testGetTriggerWithBadRegexTriggerMethod() throws Exception {
    OnNewDataContainersTriggerExtractor extractorTestProxy = spy(extractor);
    MovilizerTrigger trigger = extractorTestProxy.getTrigger(badRegexTriggerMethod, endpoints);
  }

  @Test
  public void testGetTriggerWithNonAnnonMethod() throws Exception {
    OnNewDataContainersTriggerExtractor extractorTestProxy = spy(extractor);
    MovilizerTrigger trigger = extractorTestProxy.getTrigger(nonAnnonMethod, endpoints);
    assertThat(trigger, is(nullValue()));
  }

  interface TestTriggerClass {
    Boolean mostOpenTrigger(Long systemId, String nonRelevant);

    Boolean mostRestrictiveTrigger(Long systemId, String nonRelevant);

    Boolean invalidTrigger(Long systemId, String nonRelevant);

    Boolean badRegexTrigger(Long systemId, String nonRelevant);

    Boolean nonAnnonMethod(Long systemId, String nonRelevant);
  }

}
