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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;
import com.movilizer.connectors.spring.model.impl.MovilizerAppEndpointImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;


@RunWith(JUnit4.class)
public class TriggerExtractorUtilsTest {
  private static final String endpointName1 = "endpoint-1";
  private static final String endpointSystemId1 = "1234";
  private static final String endpointName2 = "endpoint-2";
  private static final String endpointSystemId2 = "4321";

  String validPatternEndpoint = "endpoint-[1|2]";
  String invalidPatternEndpoint = "endpoint\\-3";
  String invalidRegexEndpoint = "[2-#*}";

  String validPatternSystemId = "123.";
  String invalidPatternSystemId = "3333";
  String invalidRegexSystemId = "[2-#*}";

  List<MovilizerAppEndpoint> endpoints;

  @Before
  public void setUp() throws Exception {
    endpoints = new ArrayList<>();
    MovilizerAppEndpoint endpoint1 =
        new MovilizerAppEndpointImpl(endpointName1, Long.parseLong(endpointSystemId1), "", "", "",
            "", 60L, 0, 0);
    endpoints.add(endpoint1);
    MovilizerAppEndpoint endpoint2 =
        new MovilizerAppEndpointImpl(endpointName2, Long.parseLong(endpointSystemId2), "", "", "",
            "", 60L, 0, 0);
    endpoints.add(endpoint2);
  }

  @Test
  public void testIsEndpointPatternValidWithValidPatternEndpoint() throws Exception {
    Boolean result = TriggerExtractorUtils.isEndpointPatternValid(validPatternEndpoint, endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(true));
  }

  @Test
  public void testIsEndpointPatternValidWithInvalidPatternEndpoint() throws Exception {
    Boolean result =
        TriggerExtractorUtils.isEndpointPatternValid(invalidPatternEndpoint, endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }

  @Test(expected = java.util.regex.PatternSyntaxException.class)
  public void testIsEndpointPatternValidWithInvalidRegexEndpoint() throws Exception {
    Boolean result = TriggerExtractorUtils.isEndpointPatternValid(invalidRegexEndpoint, endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }

  @Test
  public void testIsSystemIdPatternValidWithValidPatternSystemId() throws Exception {
    Boolean result = TriggerExtractorUtils.isSystemIdPatternValid(validPatternSystemId, endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(true));
  }

  @Test
  public void testIsSystemIdPatternValidWithInvalidPatternSystemId() throws Exception {
    Boolean result =
        TriggerExtractorUtils.isSystemIdPatternValid(invalidPatternSystemId, endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }

  @Test(expected = java.util.regex.PatternSyntaxException.class)
  public void testIsSystemIdPatternValidWithInvalidRegexSystemId() throws Exception {
    Boolean result = TriggerExtractorUtils.isSystemIdPatternValid(invalidRegexSystemId, endpoints);
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }
}
