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

import com.movilizer.connectors.spring.annotations.MovilizerConfig;
import com.movilizer.connectors.spring.model.exceptions.IllegalMovilizerAppConfigException;
import com.movilizer.connectors.spring.utils.ArrayUtils;
import com.movilizer.connectors.spring.utils.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * Retrieves the configuration parameters from the Movilizer App class and creates.
 *
 * @author Jesús de Mula Cano
 * @see MovilizerConfig
 * @since 0.1
 */
@Component
public class ConfigFinder {
  private static final String ERROR_FILE_NOT_READ = "Couldn't load properties file for app %s";
  private static final String PROPERTIES_EXT = ".properties";
  private static final String YAML_EXT = ".yml";
  private static final String YAML_EXT2 = ".yaml";
  private static Log logger = LogFactory.getLog(ConfigFinder.class);

  private AppInfoExtractor appInfoExtractor;

  @Autowired
  public ConfigFinder(AppInfoExtractor appInfoExtractor) {
    this.appInfoExtractor = appInfoExtractor;
  }

  /**
   * Extracts the properties of a Movilizer App from the path given in a class annotated with
   * {@link MovilizerConfig}. If no path is given the auto-discovery mechanism will use the app name
   * (explicit in the annotation or the app class name) to find the configuration file.
   * 
   * @param movilizerAppClass class containing the annotations needed for a Movilizer App.
   * @return the properties instance with the loaded config.
   */
  public Properties fromMovilizerAppClass(Class<?> movilizerAppClass)
      throws IllegalMovilizerAppConfigException {
    Properties properties;
    try {
      MovilizerConfig movilizerConfig = movilizerAppClass.getAnnotation(MovilizerConfig.class);
      if (movilizerConfig != null) {
        String configFilePath = movilizerConfig.value()[0];
        properties = findResource(configFilePath);
      } else {
        properties = automaticConfigFileDiscovery(movilizerAppClass);
      }
    } catch (IllegalStateException | IOException e) {
      String msg = String.format(ERROR_FILE_NOT_READ, movilizerAppClass.getName());
      if (logger.isErrorEnabled()) {
        logger.error(msg, e);
      }
      throw new IllegalMovilizerAppConfigException(msg, e);
    }
    return properties;
  }

  protected Properties automaticConfigFileDiscovery(Class<?> movilizerAppClass) throws IOException {
    Properties properties;
    String packageName = movilizerAppClass.getPackage().getName();
    String appName = appInfoExtractor.getAppName(movilizerAppClass);
    String appConfigFilename = StringUtils.camelCaseToHyphens(appName);
    // Try .yaml
    String[] yamlPatterns =
        getAllPossibleYamlConfigFilePathPatterns(appConfigFilename, packageName);
    properties = findYmlResource(yamlPatterns);
    if (properties.isEmpty()) {
      // Try .properties
      String[] propsPatterns =
          getAllPossiblePropertiesConfigFilePathPatterns(appConfigFilename, packageName);
      properties = findPropertiesResource(propsPatterns);
    }
    return properties;
  }

  protected String[] getAllPossibleYamlConfigFilePathPatterns(String appName, String packageName) {
    String[] out = new String[4];
    out[0] = StringUtils.composeClasspathSearchPattern(appName, YAML_EXT, "");
    out[1] = StringUtils.composeClasspathSearchPattern(appName, YAML_EXT2, "");
    out[2] =
        StringUtils.composeClasspathSearchPatternFollowChildren(appName, YAML_EXT, packageName);
    out[3] =
        StringUtils.composeClasspathSearchPatternFollowChildren(appName, YAML_EXT2, packageName);
    return out;
  }

  protected String[] getAllPossiblePropertiesConfigFilePathPatterns(String appName,
      String packageName) {
    String[] out = new String[2];
    out[0] = StringUtils.composeClasspathSearchPattern(appName, PROPERTIES_EXT, "");
    out[1] =
        StringUtils.composeClasspathSearchPatternFollowChildren(appName, PROPERTIES_EXT,
            packageName);
    return out;
  }

  protected Properties findResource(String searchPattern) throws IOException {
    Properties properties = null;
    if (isPropertiesFile(searchPattern)) {
      properties = findPropertiesResource(searchPattern);
    } else if (isYamlFile(searchPattern)) {
      properties = findYmlResource(searchPattern);
    }
    return properties;
  }

  protected Properties findYmlResource(String... searchPattern) throws IOException {
    YamlPropertiesFactoryBean yamlReader = new YamlPropertiesFactoryBean();
    yamlReader.setResources(mergeResources(searchPattern));
    yamlReader.afterPropertiesSet();
    return yamlReader.getObject();
  }

  protected Properties findPropertiesResource(String... searchPattern) throws IOException {
    PropertiesFactoryBean propertiesReader = new PropertiesFactoryBean();
    propertiesReader.setLocations(mergeResources(searchPattern));
    propertiesReader.afterPropertiesSet();
    return propertiesReader.getObject();
  }

  protected Resource[] mergeResources(String... searchPatterns) throws IOException {
    Resource[] out = new Resource[0];
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    for (String searchPattern : searchPatterns) {
      out = ArrayUtils.addAll(out, resourcePatternResolver.getResources(searchPattern));
    }
    return out;
  }

  protected String getSearchPattern(String filename, String fileExtension, String basePackageToScan) {
    return StringUtils.composeClasspathSearchPatternFollowChildren(filename, fileExtension,
        basePackageToScan);
  }

  protected boolean isYamlFile(String filename) {
    return StringUtils.endsWithIgnoreCase(filename, YAML_EXT)
        || StringUtils.endsWithIgnoreCase(filename, YAML_EXT2);
  }

  protected boolean isPropertiesFile(String filename) {
    return StringUtils.endsWithIgnoreCase(filename, PROPERTIES_EXT);
  }
}
