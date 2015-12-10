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

package com.javafxpert.wikibrowser.model.idlocator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LangLinkFar {
  @JsonProperty("lang")
  private String langCode;

  @JsonProperty("url")
  private String articleUrl;

  @JsonProperty("langname")
  private String langName;

  @JsonProperty("autonym")
  private String langAutonym;

  @JsonProperty("title")
  private String articleTitle;

  public LangLinkFar() {
  }

  public LangLinkFar(String langCode, String articleUrl, String langName, String langAutonym, String articleTitle) {
    this.langCode = langCode;
    this.articleUrl = articleUrl;
    this.langName = langName;
    this.langAutonym = langAutonym;
    this.articleTitle = articleTitle;
  }

  public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
  }

  public String getArticleUrl() {
    return articleUrl;
  }

  public void setArticleUrl(String articleUrl) {
    this.articleUrl = articleUrl;
  }

  public String getLangName() {
    return langName;
  }

  public void setLangName(String langName) {
    this.langName = langName;
  }

  public String getLangAutonym() {
    return langAutonym;
  }

  public void setLangAutonym(String langAutonym) {
    this.langAutonym = langAutonym;
  }

  public String getArticleTitle() {
    return articleTitle;
  }

  public void setArticleTitle(String articleTitle) {
    this.articleTitle = articleTitle;
  }

  @Override
  public String toString() {
    return "LangLinkFar{" +
        "langCode='" + langCode + '\'' +
        ", articleUrl='" + articleUrl + '\'' +
        ", langName='" + langName + '\'' +
        ", langAutonym='" + langAutonym + '\'' +
        ", articleTitle='" + articleTitle + '\'' +
        '}';
  }
}


