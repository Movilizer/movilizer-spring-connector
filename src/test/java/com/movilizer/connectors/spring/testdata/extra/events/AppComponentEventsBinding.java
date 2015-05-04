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

package com.movilizer.connectors.spring.testdata.extra.events;

import com.movilitas.movilizer.v12.MovilizerStatusMessage;
import com.movilitas.movilizer.v12.MovilizerUploadDataContainer;
import com.movilizer.connectors.spring.annotations.MovilizerComponent;
import com.movilizer.connectors.spring.annotations.triggers.OnNewDataContainers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;


@MovilizerComponent
public class AppComponentEventsBinding {

  private AutowiredPrinterService printer;

  @Autowired
  public AppComponentEventsBinding(AutowiredPrinterService printer) {
    this.printer = printer;
  }

  @OnNewDataContainers
  private void processAllNewDataContainers(List<MovilizerUploadDataContainer> uploadContainer,
      List<MovilizerStatusMessage> requestStatusMessages, Long systemId, String responseQueue,
      Calendar mdsServerTime, Calendar connectorServerTime, Short containerUploadPriority,
      String key, String moveletKey, String moveletKeyExtension, Long moveletVersion,
      String participantKey, String deviceAddress) {
    printer.print(String.format("systemId: %d, key: %s, movelet: %s/%s-%d, participant: %s(%s)",
        systemId, key, moveletKey, moveletKeyExtension, moveletVersion, deviceAddress,
        participantKey));
  }

  @OnNewDataContainers(systemId = "1234")
  private void processFromSystemId(List<MovilizerUploadDataContainer> uploadContainer) {
    printer.print("datacontainers list size: " + uploadContainer.size());
  }
}
