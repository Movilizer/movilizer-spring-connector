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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.movilitas.movilizer.v12.MovilizerGenericUploadDataContainer;
import com.movilitas.movilizer.v12.MovilizerResponse;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connectors.spring.model.events.MovilizerEvent;
import com.movilizer.connectors.spring.model.events.NewDataContainerEvent;
import com.movilizer.connectors.spring.model.impl.TriggerFilterParams;
import com.movilizer.connectors.spring.model.triggers.MovilizerTrigger;
import com.movilizer.connectors.spring.model.triggers.NewDataContainerTrigger;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;
import java.util.Calendar;

@RunWith(JUnit4.class)
public class NewDataContainerEventTriggerTest {
  private static final Long eventSystemId = 1234L;
  private static Log logger = LogFactory.getLog(NewDataContainerEventTriggerTest.class);

  @Test
  public void testFire() throws Exception {
    // Setup
    final Boolean[] successfullyFired = {false};
    final Long[] systemIdInjected = {0L};
    TestFireClass myTestFireClass = new TestFireClass() {
      @Override
      public Boolean triggerTargetMethod(Long systemId, String nonRelevant) {
        logger.info(String.format(
            "triggerTargetMethod has been invoked. System id: %d, Non relevant: %s", systemId,
            nonRelevant));
        systemIdInjected[0] = systemId;
        successfullyFired[0] = true;
        return true;
      }
    };

    Method targetMethod =
        myTestFireClass.getClass().getDeclaredMethod("triggerTargetMethod", Long.class,
            String.class);

    Calendar serverTime = Calendar.getInstance();
    MovilizerResponse response = new MovilizerResponse();
    response.setSystemId(eventSystemId);
    response.setCurrentServerTime(new XMLGregorianCalendarImpl());
    MovilizerUploadDataContainer uploadDataContainer = new MovilizerUploadDataContainer();
    MovilizerGenericUploadDataContainer dataContainer = new MovilizerGenericUploadDataContainer();
    dataContainer.setKey("key");
    dataContainer.setMoveletKey("mKey");
    dataContainer.setMoveletKeyExtension("DEMO");
    dataContainer.setMoveletVersion(12L);
    dataContainer.setDeviceAddress("+9991337");
    dataContainer.setParticipantKey("123456");
    uploadDataContainer.setContainer(dataContainer);
    MovilizerAppEndpoint endpoint = mock(MovilizerAppEndpoint.class);
    when(endpoint.getName()).thenReturn("my-endpoint");

    MovilizerEvent event =
        new NewDataContainerEvent(endpoint, response, uploadDataContainer, serverTime);


    MovilizerFilterParams filter =
        new TriggerFilterParams("", String.valueOf(eventSystemId), "", "", "", "", "", "", "", "");
    MovilizerTrigger trigger = new NewDataContainerTrigger(-1, filter, targetMethod);
    trigger.setBean(myTestFireClass);
    // Preconditions
    assertThat(successfullyFired[0], is(false));
    assertThat(systemIdInjected[0], is(0L));
    assertThat(event.getSystemId(), is(eventSystemId));
    // Check fire outcome
    trigger.fire(event);
    assertThat(successfullyFired[0], is(true));
    assertThat(systemIdInjected[0], is(eventSystemId));
  }

  interface TestFireClass {
    Boolean triggerTargetMethod(Long systemId, String nonRelevant);
  }

}
