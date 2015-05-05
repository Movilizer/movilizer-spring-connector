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

package com.movilizer.connectors.spring.init;

import com.movilizer.connectors.spring.model.MovilizerAppContext;
import com.movilizer.connectors.spring.model.MovilizerContext;
import com.movilizer.connectors.spring.model.impl.MovilizerContextImpl;
import com.movilizer.connectors.spring.scanning.AppScanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@Lazy
@PropertySource("classpath:META-INF/movilizer/connector-config.yml")
@ComponentScan(basePackageClasses = {AppScanner.class})
public class MovilizerConfig {
  public static final String APP_SCANNER_BEAN = "movilizerAppScanner";

  @Autowired
  private AppScanner appScanner;
  @Value("${movilizer.apps.package}")
  private String appsBasePackage;

  @Bean
  @DependsOn(APP_SCANNER_BEAN)
  public MovilizerContext movilizerContext() {
    List<MovilizerAppContext> apps = appScanner.getApps();
    return new MovilizerContextImpl(appsBasePackage, apps);
  }
}
