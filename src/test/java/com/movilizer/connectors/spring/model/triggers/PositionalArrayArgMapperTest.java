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

package com.movilizer.connectors.spring.model.triggers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

@RunWith(JUnit4.class)
public class PositionalArrayArgMapperTest {

  Method sourceMethod;
  Method targetMethod;
  int[] expectedMapping = {PositionalArrayArgMapper.NO_POSITION_FOUND, 0};
  PositionalArrayArgMapper mapper;
  ParameterNameDiscoverer parameterNameDiscoverer;

  @Before
  public void setUp() throws Exception {
    parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    Source source = new Source() {
      @Override
      public Boolean sourceMethod(Long number, String nonRelevant) {
        return null;
      }
    };
    Target target = new Target() {
      @Override
      public void targetMethod(Integer ignore, Long number) {

      }
    };
    sourceMethod = source.getClass().getDeclaredMethod("sourceMethod", Long.class, String.class);
    targetMethod = target.getClass().getDeclaredMethod("targetMethod", Integer.class, Long.class);
    mapper = new PositionalArrayArgMapper(sourceMethod, targetMethod);
  }

  @Test
  public void testPositionsMapping() throws Exception {
    int[] targetToSourcePositions = mapper.positionsMapping(sourceMethod, targetMethod);
    assertThat(targetToSourcePositions.length, is(2));
    assertThat(targetToSourcePositions[0], is(expectedMapping[0]));
    assertThat(targetToSourcePositions[1], is(expectedMapping[1]));
  }

  interface Source {
    Boolean sourceMethod(Long number, String nonRelevant);
  }

  interface Target {
    void targetMethod(Integer ignore, Long number);
  }
}
