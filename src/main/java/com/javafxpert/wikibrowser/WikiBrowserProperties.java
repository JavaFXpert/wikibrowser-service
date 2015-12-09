/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javafxpert.wikibrowser;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author James Weaver
 */
@ConfigurationProperties(prefix = "wikibrowserservice")
@Component
public class WikiBrowserProperties {

  private String host;
  private String locatorEndpoint;
  private String lang;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getLocatorEndpoint() {
    return locatorEndpoint;
  }

  public void setLocatorEndpoint(String locatorEndpoint) {
    this.locatorEndpoint = locatorEndpoint;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String computeLang(String forceLang) {
    String language = "en"; // Fallback value if not passed in or available in property
    if (forceLang != null && forceLang.length() > 0) {
      // If forceLand is passed in, use it
      language = lang;
    }
    else if (this.lang != null && this.lang.length() > 0) {
      // If lang not passed in, then use the property if available
      language = this.lang;
    }
    return language;
  }

  /**
   * Provide the URL to the locator service method
   *
   */
  public String getLocatorServiceUrl(String itemId, String lang) {
    String url = this.host + String.format(this.getLocatorEndpoint(), itemId, computeLang(lang));
    return url;
  }
}
