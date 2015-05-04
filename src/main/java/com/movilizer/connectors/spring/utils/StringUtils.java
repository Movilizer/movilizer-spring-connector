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

import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shared tools for working with strings across the connector.
 *
 * @author Jesús de Mula Cano
 * @since 0.1
 */
public abstract class StringUtils {
  private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");

  /**
   * From {@code org.springframework.boot.bind.RelaxedNames.Manipulation#CAMELCASE_TO_HYPHEN}.
   * 
   * @param camelCaseString the string to convert to hyphens.
   * @return string with the correct hyphens instead of capital letters.
   */
  public static String camelCaseToHyphens(String camelCaseString) {
    Matcher matcher = CAMEL_CASE_PATTERN.matcher(camelCaseString);
    StringBuffer result = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(result, matcher.group(1) + '-'
          + org.springframework.util.StringUtils.uncapitalize(matcher.group(2)));
    }
    matcher.appendTail(result);
    return org.springframework.util.StringUtils.uncapitalize(result.toString());
  }

  /**
   * From {@code org.springframework.util.StringUtils#endsWithIgnoreCase}.
   * <p/>
   * Test if the given String ends with the specified suffix, ignoring upper/lower case.
   * 
   * @param str the String to check
   * @param suffix the suffix to look for
   * @see org.springframework.util.StringUtils#endsWithIgnoreCase
   * @see java.lang.String#endsWith
   */
  public static boolean endsWithIgnoreCase(String str, String suffix) {
    return org.springframework.util.StringUtils.endsWithIgnoreCase(str, suffix);
  }

  /**
   * From {@code org.springframework.util.StringUtils#startsWithIgnoreCase}.
   * <p/>
   * Test if the given String starts with the specified prefix, ignoring upper/lower case.
   * 
   * @param str the String to check
   * @param prefix the prefix to look for
   * @see java.lang.String#startsWith
   */
  public static boolean startsWithIgnoreCase(String str, String prefix) {
    return org.springframework.util.StringUtils.startsWithIgnoreCase(str, prefix);
  }

  /**
   * Creates a valid search pattern to locate resources in the classpath. E.g.: {code
   * filename="example-app-name"; fileExtension="yml"; basePackageToScan="com.movilizer.test"} will
   * output {@code "classpath*:com/movilizer/test/**&#47example-app-name.yml"}.
   * 
   * @param filename the filename for the specific search.
   * @param fileExtension the extension for the specific search.
   * @param basePackageToScan the package that will be the root of the search.
   * @return valid string to use in a resource locator finder such as
   *         {@code org.springframework.core.io.support.ResourcePatternResolver#getResources}.
   */
  public static String composeClasspathSearchPatternFollowChildren(String filename,
      String fileExtension, String basePackageToScan) {
    return ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
        + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
            .resolvePlaceholders(basePackageToScan)) + "/**/" + filename + fileExtension;
  }

  /**
   * Creates a valid search pattern to locate resources in the classpath. E.g.: {code
   * filename="example-app-name"; fileExtension="yml"; basePackageToScan="com.movilizer.test"} will
   * output {@code "classpath*:com/movilizer/test/**&#47example-app-name.yml"}.
   *
   * @param filename the filename for the specific search.
   * @param fileExtension the extension for the specific search.
   * @param basePackageToScan the package that will be the root of the search.
   * @return valid string to use in a resource locator finder such as
   *         {@code org.springframework.core.io.support.ResourcePatternResolver#getResources}.
   */
  public static String composeClasspathSearchPattern(String filename, String fileExtension,
      String basePackageToScan) {
    return ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
        + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
            .resolvePlaceholders(basePackageToScan)) + "/" + filename + fileExtension;
  }

  /**
   * From {@code org.springframework.util.StringUtils#collectionToDelimitedString}.
   * <p/>
   * Convenience method to return a Collection as a CSV String. E.g. useful for {@code toString()}
   * implementations.
   * 
   * @param coll the Collection to display
   * @return the delimited String
   */
  public static String collectionToCommaDelimitedString(Collection<?> coll) {
    return org.springframework.util.StringUtils.collectionToCommaDelimitedString(coll);
  }

}
