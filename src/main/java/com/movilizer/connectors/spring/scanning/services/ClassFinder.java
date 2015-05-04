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

import com.movilizer.connectors.spring.utils.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper service to deal with finding classes given an annotation they might have.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
@Component
public class ClassFinder {
  private static Log logger = LogFactory.getLog(ClassFinder.class);

  /**
   * Look for classes having the specified annotation in a given package and its children.
   * 
   * @param annotation the annotation that the class has to be annotated with.
   * @param basePackageToScan base package to start looking for annotated classes.
   * @return a list with the classes found.
   */
  public <T extends Annotation> Set<Class<?>> findClassesAnnotatedWith(Class<T> annotation,
      String basePackageToScan) {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    MetadataReaderFactory metadataReaderFactory =
        new CachingMetadataReaderFactory(resourcePatternResolver);
    Set<Class<?>> classesWithTheAnnotation = new HashSet<>();

    String packageSearchPath =
        StringUtils.composeClasspathSearchPatternFollowChildren("*", ".class", basePackageToScan);

    try {
      Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
      for (Resource resource : resources) {
        if (resource.isReadable()) {
          MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
          Class candidate = Class.forName(metadataReader.getClassMetadata().getClassName());
          if (hasAnnotation(candidate, annotation)) {
            classesWithTheAnnotation.add(candidate);
          }

        }
      }
    } catch (ClassNotFoundException | IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error("Error while looking for Movilizer apps in the package: " + basePackageToScan,
            e);
      }
    }
    return classesWithTheAnnotation;
  }

  protected boolean hasAnnotation(Class candidate, Class annotation) throws ClassNotFoundException {
    return candidate.getAnnotation(annotation) != null;
  }
}
