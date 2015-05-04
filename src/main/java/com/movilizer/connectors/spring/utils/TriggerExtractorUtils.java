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

import com.movilizer.connectors.spring.model.MovilizerAppEndpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Shared tools for working with Movilizer triggers across the connector.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public abstract class TriggerExtractorUtils {
  // private static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

  /**
   * Checks if the endpoint name pattern given is acceptable given the endpoints in scope.
   *
   * @param pattern to inspect.
   * @param endpoints available in scope.
   * @return true if valid else false.
   */
  public static Boolean isEndpointPatternValid(String pattern, List<MovilizerAppEndpoint> endpoints)
      throws PatternSyntaxException {
    if ("".equals(pattern)) {
      return true;
    }
    Matcher matcher;
    Pattern patternCompiled = Pattern.compile(pattern);
    for (MovilizerAppEndpoint endpoint : endpoints) {
      matcher = patternCompiled.matcher(endpoint.getName());
      if (matcher.matches()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the system id pattern given is acceptable given the endpoints in scope.
   *
   * @param pattern to inspect.
   * @param endpoints available in scope.
   * @return true if valid else false.
   */
  public static Boolean isSystemIdPatternValid(String pattern, List<MovilizerAppEndpoint> endpoints)
      throws PatternSyntaxException {
    if ("".equals(pattern)) {
      return true;
    }
    Matcher matcher;
    Pattern patternCompiled = Pattern.compile(pattern);
    for (MovilizerAppEndpoint endpoint : endpoints) {
      matcher = patternCompiled.matcher(String.valueOf(endpoint.getSystemId()));
      if (matcher.matches()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gives a list of the valid endpoint names in string format for printing messages.
   * 
   * @param endpoints available endpoints.
   * @return list of endpoint names which are valid.
   */
  public static List<String> validEndpointNames(List<MovilizerAppEndpoint> endpoints) {
    List<String> validEndpointNames = new ArrayList<>();
    for (MovilizerAppEndpoint endpoint : endpoints) {
      validEndpointNames.add(endpoint.getName());
    }
    return validEndpointNames;
  }

  /**
   * Gives a list of the valid system ids in string format for printing messages.
   *
   * @param endpoints available endpoints.
   * @return list of system ids which are valid.
   */
  public static List<String> validSystemIds(List<MovilizerAppEndpoint> endpoints) {
    List<String> validSystemIds = new ArrayList<>();
    for (MovilizerAppEndpoint endpoint : endpoints) {
      validSystemIds.add(String.valueOf(endpoint.getSystemId()));
    }
    return validSystemIds;
  }

  // private String escapeSpecialRegexChars(String str) {
  // return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
  // }
}
