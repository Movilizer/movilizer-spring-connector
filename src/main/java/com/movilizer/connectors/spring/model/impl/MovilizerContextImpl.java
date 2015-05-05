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

package com.movilizer.connectors.spring.model.impl;

import com.movilizer.connectors.spring.model.MovilizerAppContext;
import com.movilizer.connectors.spring.model.MovilizerContext;

import java.util.List;

public class MovilizerContextImpl implements MovilizerContext {
  private static final String VERSION = "Movilizer Spring connector v1.0";

  String appBaseScanPackage;
  List<MovilizerAppContext> apps;

  public MovilizerContextImpl(String appBaseScanPackage, List<MovilizerAppContext> apps) {
    this.appBaseScanPackage = appBaseScanPackage;
    this.apps = apps;
  }

  @Override
  public List<MovilizerAppContext> getApps() {
    return apps;
  }

  @Override
  public String getVersion() {
    return VERSION;
  }

  @Override
  public String getAppBaseScanPackage() {
    return appBaseScanPackage;
  }

}
