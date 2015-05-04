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

package com.movilizer.connectors.spring.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.movilizer.connectors.spring.model.impl.EventFilterParams;
import com.movilizer.connectors.spring.model.impl.TriggerFilterParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class MovilizerFilterParamsTest {
  private final static String EMPTY_FILTER = "";

  MovilizerAppContext appContext;
  MovilizerAppEndpoint endpointOne;
  String endpointOneName = "One";
  Long endpointOneSystemId = 1234L;
  MovilizerAppEndpoint endpointTwo;
  String endpointTwoName = "Two";
  Long endpointTwoSystemId = 4321L;

  String keyRegex = "com\\.datacontainer\\.key\\-.*";
  String keyEventValue = "com.datacontainer.key-1234";
  String keyEventValueNotIncluded = "not.com.datacontainer.key-1234";

  @Before
  public void setUp() throws Exception {
    appContext = mock(MovilizerAppContext.class);
    endpointOne = mock(MovilizerAppEndpoint.class);
    endpointTwo = mock(MovilizerAppEndpoint.class);
  }

  @Test
  public void testDefaultsCorrectlySetForTrigger() throws Exception {
    when(endpointOne.getName()).thenReturn(endpointOneName);
    when(endpointOne.getSystemId()).thenReturn(endpointOneSystemId);
    when(endpointTwo.getName()).thenReturn(endpointTwoName);
    when(endpointTwo.getSystemId()).thenReturn(endpointTwoSystemId);
    when(appContext.getEndpoints()).thenReturn(Arrays.asList(endpointOne, endpointTwo));

    TriggerFilterParams filterParams =
        new TriggerFilterParams(EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER,
            EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER);

    filterParams.applyDefaults(appContext.getEndpoints());
    assertThat(filterParams.getEndpoint(), is(endpointOneName + TriggerFilterParams.OR_REGEX_SYMBOL
        + endpointTwoName));
    assertThat(filterParams.getSystemId(), is(String.valueOf(endpointOneSystemId)
        + TriggerFilterParams.OR_REGEX_SYMBOL + String.valueOf(endpointTwoSystemId)));
    assertThat(filterParams.getResponseQueue(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(filterParams.getContainerUploadPriority(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(filterParams.getKey(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(filterParams.getMoveletKey(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(filterParams.getMoveletKeyExtension(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(filterParams.getMoveletVersion(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(filterParams.getParticipantKey(), is(MovilizerFilterParams.WILDCARD_REGEX));
    assertThat(filterParams.getDeviceAddress(), is(MovilizerFilterParams.WILDCARD_REGEX));
  }

  @Test
  public void testDefaultsTriggerFiresForCorrectEndpointAndSystemId() throws Exception {
    when(endpointOne.getName()).thenReturn(endpointOneName);
    when(endpointOne.getSystemId()).thenReturn(endpointOneSystemId);
    when(endpointTwo.getName()).thenReturn(endpointTwoName);
    when(endpointTwo.getSystemId()).thenReturn(endpointTwoSystemId);
    when(appContext.getEndpoints()).thenReturn(Arrays.asList(endpointOne, endpointTwo));

    TriggerFilterParams filterParams =
        new TriggerFilterParams(EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER,
            EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER);

    filterParams.applyDefaults(appContext.getEndpoints());

    MovilizerFilterParams eventParams =
        new EventFilterParams(endpointTwoName, String.valueOf(endpointTwoSystemId), "", "",
            "my.datacontainer.key", "my.movelet.key", "DEMO", "12", "12345", "+9991337");
    Boolean isEqual = filterParams.isEqual(eventParams);

    assertThat(isEqual, is(notNullValue()));
    assertThat(isEqual, is(true));
  }

  @Test
  public void testDefaultsTriggerDoesNotFireForIncorrectEndpointAndCorrectSystemId()
      throws Exception {
    when(endpointOne.getName()).thenReturn(endpointOneName);
    when(endpointOne.getSystemId()).thenReturn(endpointOneSystemId);
    when(endpointTwo.getName()).thenReturn(endpointTwoName);
    when(endpointTwo.getSystemId()).thenReturn(endpointTwoSystemId);
    when(appContext.getEndpoints()).thenReturn(Arrays.asList(endpointOne, endpointTwo));

    TriggerFilterParams filterParams =
        new TriggerFilterParams(EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER,
            EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER);

    filterParams.applyDefaults(appContext.getEndpoints());

    MovilizerFilterParams eventParams =
        new EventFilterParams("not-my-endpoint", String.valueOf(endpointTwoSystemId), "", "",
            "my.datacontainer.key", "my.movelet.key", "DEMO", "12", "12345", "+9991337");
    Boolean isEqual = filterParams.isEqual(eventParams);

    assertThat(isEqual, is(notNullValue()));
    assertThat(isEqual, is(false));
  }

  @Test
  public void testDefaultsTriggerDoesNotFireForCorrectEndpointAndIncorrectSystemId()
      throws Exception {
    when(endpointOne.getName()).thenReturn(endpointOneName);
    when(endpointOne.getSystemId()).thenReturn(endpointOneSystemId);
    when(endpointTwo.getName()).thenReturn(endpointTwoName);
    when(endpointTwo.getSystemId()).thenReturn(endpointTwoSystemId);
    when(appContext.getEndpoints()).thenReturn(Arrays.asList(endpointOne, endpointTwo));

    TriggerFilterParams filterParams =
        new TriggerFilterParams(EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER,
            EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER);

    filterParams.applyDefaults(appContext.getEndpoints());

    MovilizerFilterParams eventParams =
        new EventFilterParams(endpointTwoName, "1337", "", "", "my.datacontainer.key",
            "my.movelet.key", "DEMO", "12", "12345", "+9991337");
    Boolean isEqual = filterParams.isEqual(eventParams);

    assertThat(isEqual, is(notNullValue()));
    assertThat(isEqual, is(false));
  }

  @Test
  public void testTriggerFiresWithCorrectDatacontainerKeyRegex() throws Exception {
    when(endpointOne.getName()).thenReturn(endpointOneName);
    when(endpointOne.getSystemId()).thenReturn(endpointOneSystemId);
    when(endpointTwo.getName()).thenReturn(endpointTwoName);
    when(endpointTwo.getSystemId()).thenReturn(endpointTwoSystemId);
    when(appContext.getEndpoints()).thenReturn(Arrays.asList(endpointOne, endpointTwo));

    TriggerFilterParams filterParams =
        new TriggerFilterParams(EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, keyRegex,
            EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER);

    filterParams.applyDefaults(appContext.getEndpoints());

    MovilizerFilterParams eventParams =
        new EventFilterParams(endpointTwoName, String.valueOf(endpointTwoSystemId), "", "",
            keyEventValue, "my.movelet.key", "DEMO", "12", "12345", "+9991337");
    Boolean isEqual = filterParams.isEqual(eventParams);

    assertThat(isEqual, is(notNullValue()));
    assertThat(isEqual, is(true));
  }

  @Test
  public void testTriggerDoesNotFireWithIncorrectDatacontainerKeyRegex() throws Exception {
    when(endpointOne.getName()).thenReturn(endpointOneName);
    when(endpointOne.getSystemId()).thenReturn(endpointOneSystemId);
    when(endpointTwo.getName()).thenReturn(endpointTwoName);
    when(endpointTwo.getSystemId()).thenReturn(endpointTwoSystemId);
    when(appContext.getEndpoints()).thenReturn(Arrays.asList(endpointOne, endpointTwo));

    TriggerFilterParams filterParams =
        new TriggerFilterParams(EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, keyRegex,
            EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER, EMPTY_FILTER);

    filterParams.applyDefaults(appContext.getEndpoints());

    MovilizerFilterParams eventParams =
        new EventFilterParams(endpointTwoName, String.valueOf(endpointTwoSystemId), "", "",
            keyEventValueNotIncluded, "my.movelet.key", "DEMO", "12", "12345", "+9991337");
    Boolean isEqual = filterParams.isEqual(eventParams);

    assertThat(isEqual, is(notNullValue()));
    assertThat(isEqual, is(false));
  }
}
