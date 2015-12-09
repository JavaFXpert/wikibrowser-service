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

  private String wikiBrowserServiceHost;

  private String lang;

  public String getWikiBrowserServiceHost() {
    return wikiBrowserServiceHost;
  }

  public void setWikiBrowserServiceHost(String wikiBrowserServiceHost) {
    this.wikiBrowserServiceHost = wikiBrowserServiceHost;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  /**
   * Provide a route to the locator service method
   *
   * @param route a relative URI to the service endpoint for the locator service
   * @param param parameters for the route's query string
   * @return a fully qualified URI to the desired service route
   */
  public String getLocatorServiceEndpoint(String route, String... param) {
    String routeAndParams = param != null ? String.format(route, (Object)param) : route;
    return String.format("%s%s", this.wikiBrowserServiceHost, routeAndParams);
  }
}
