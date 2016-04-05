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
@ConfigurationProperties(prefix = "conceptmap")
@Component
public class WikiBrowserProperties {

  private String host;
  private String locatorEndpoint;
  private String lang;

  private String thumbnailByTitleEndpoint;
  private String thumbnailByIdEndpoint;

  private String cypherUsername;
  private String cypherPassword;

  private String cypherHost;
  private String cypherPort;
  private String cypherEndpoint;

  private String blat;

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

  public String getThumbnailByTitleEndpoint() {
    return thumbnailByTitleEndpoint;
  }

  public void setThumbnailByTitleEndpoint(String thumbnailByTitleEndpoint) {
    this.thumbnailByTitleEndpoint = thumbnailByTitleEndpoint;
  }

  public String getThumbnailByIdEndpoint() {
    return thumbnailByIdEndpoint;
  }

  public void setThumbnailByIdEndpoint(String thumbnailByIdEndpoint) {
    this.thumbnailByIdEndpoint = thumbnailByIdEndpoint;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getCypherHost() {
    return cypherHost;
  }

  public void setCypherHost(String cypherHost) {
    this.cypherHost = cypherHost;
  }

  public String getCypherPort() {
    return cypherPort;
  }

  public void setCypherPort(String cypherPort) {
    this.cypherPort = cypherPort;
  }

  public String getCypherEndpoint() {
    return cypherEndpoint;
  }

  public void setCypherEndpoint(String cypherEndpoint) {
    this.cypherEndpoint = cypherEndpoint;
  }

  public String getCypherUsername() {
    return cypherUsername;
  }

  public void setCypherUsername(String cypherUsername) {
    this.cypherUsername = cypherUsername;
  }

  public String getCypherPassword() {
    return cypherPassword;
  }

  public void setCypherPassword(String cypherPassword) {
    this.cypherPassword = cypherPassword;
  }

  public String getBlat() {
    return blat;
  }

  public void setBlat(String blat) {
    this.blat = blat;
  }

  public String computeLang(String forceLang) {
    String language = "en"; // Fallback value if not passed in or available in property
    if (forceLang != null && forceLang.length() > 0) {
      // If forceLand is passed in, use it
      language = forceLang;
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

  /**
   * Provide the URL to the thumbnail service method
   *
   */
  public String getThumbnailByTitleServiceUrl(String articleTitle, String lang) {
    String url = this.host + String.format(this.getThumbnailByTitleEndpoint(), articleTitle, computeLang(lang));
    return url;
  }

  /**
   * Provide the URL to the thumbnail service method
   *
   */
  public String getThumbnailByIdServiceUrl(String itemId, String lang) {
    String url = this.host + String.format(this.getThumbnailByIdEndpoint(), itemId, computeLang(lang));
    return url;
  }

  /**
   * Provide the URL to the Neo4j Transactional Cypher HTTP endpoint
   *
   */
  public String getNeoCypherUrl() {
    String url = "https://" + this.cypherHost + ":" + this.cypherPort + this.cypherEndpoint;
    return url;
  }
}
