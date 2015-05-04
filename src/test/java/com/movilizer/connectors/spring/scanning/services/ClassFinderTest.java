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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.movilizer.connectors.spring.annotations.MovilizerApp;
import com.movilizer.connectors.spring.annotations.MovilizerComponent;
import com.movilizer.connectors.spring.testdata.apps.CompleteScanningTestApp;
import com.movilizer.connectors.spring.testdata.apps.MinimalScanningTestApp;
import com.movilizer.connectors.spring.testdata.apps.NonExistingConfigTestApp;
import com.movilizer.connectors.spring.testdata.apps.NonValidConfigTestApp;
import com.movilizer.connectors.spring.testdata.apps.integration.AverageApp;
import com.movilizer.connectors.spring.testdata.extra.classes.one.AppComponentClassOne;
import com.movilizer.connectors.spring.testdata.extra.classes.one.three.AppComponentClassThree;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Set;

@RunWith(JUnit4.class)
public class ClassFinderTest {

  String appPackage;
  String appClassPackage;
  String appClassInnerPackage;
  ClassFinder classFinder;

  @Before
  public void setUp() throws Exception {
    appPackage = "com.movilizer.connectors.spring.testdata";
    appClassPackage = AppComponentClassOne.class.getPackage().getName();
    appClassInnerPackage = AppComponentClassThree.class.getPackage().getName();
    classFinder = new ClassFinder();
  }

  @Test
  public void testFindMovilizerAppsInPackage() throws Exception {
    Set<Class<?>> classesWithMovilizerAppAnnotation =
        classFinder.findClassesAnnotatedWith(MovilizerApp.class, appPackage);
    assertThat(classesWithMovilizerAppAnnotation, is(notNullValue()));
    assertThat(classesWithMovilizerAppAnnotation.size(), is(5));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(MinimalScanningTestApp.class));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(CompleteScanningTestApp.class));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(NonExistingConfigTestApp.class));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(NonValidConfigTestApp.class));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(AverageApp.class));
  }

  @Test
  public void testComponentIsFoundWhenSpecifyingItsPackageAsBase() throws Exception {
    Set<Class<?>> classesWithMovilizerAppAnnotation =
        classFinder.findClassesAnnotatedWith(MovilizerComponent.class, appClassInnerPackage);
    assertThat(classesWithMovilizerAppAnnotation, is(notNullValue()));
    assertThat(classesWithMovilizerAppAnnotation.size(), is(1));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(AppComponentClassThree.class));
  }

  @Test
  public void testComponentAreFoundWhenNested() throws Exception {
    Set<Class<?>> classesWithMovilizerAppAnnotation =
        classFinder.findClassesAnnotatedWith(MovilizerComponent.class, appClassPackage);
    assertThat(classesWithMovilizerAppAnnotation, is(notNullValue()));
    assertThat(classesWithMovilizerAppAnnotation.size(), is(2));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(AppComponentClassOne.class));
    assertThat(classesWithMovilizerAppAnnotation, hasItem(AppComponentClassThree.class));
  }

  @Test
  public void testEmptyListWhenPackageDoesNotExists() throws Exception {
    Set<Class<?>> classesWithMovilizerAppAnnotation =
        classFinder.findClassesAnnotatedWith(MovilizerComponent.class, "no.package.for.you");
    assertThat(classesWithMovilizerAppAnnotation, is(notNullValue()));
    assertThat(classesWithMovilizerAppAnnotation.isEmpty(), is(true));
  }
}
