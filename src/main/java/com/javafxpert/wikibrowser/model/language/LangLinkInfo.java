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

package com.javafxpert.wikibrowser.model.language;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author James Weaver
 *
 */
@JsonPropertyOrder({"articleTitle", "articleName", "articleUrl", "langCode", "langName", "langAutonym"})
public class LangLinkInfo {

  private String articleTitle;
  private String articleName;
  private String articleUrl;
  private String langCode;
  private String langName;
  private String langAutonym;

  public LangLinkInfo(String articleTitle, String articleName, String articleUrl, String langCode, String langName, String langAutonym) {
    this.articleTitle = articleTitle;
    this.articleName = articleName;
    this.articleUrl = articleUrl;
    this.langCode = langCode;
    this.langName = langName;
    this.langAutonym = langAutonym;
  }

  public LangLinkInfo() {
  }

  public String getArticleTitle() {
    return articleTitle;
  }

  public void setArticleTitle(String articleTitle) {
    this.articleTitle = articleTitle;
  }

  public String getArticleName() {
    return articleName;
  }

  public void setArticleName(String articleName) {
    this.articleName = articleName;
  }

  public String getArticleUrl() {
    return articleUrl;
  }

  public void setArticleUrl(String articleUrl) {
    this.articleUrl = articleUrl;
  }

  public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
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

  @Override
  public String toString() {
    return "LangLinkInfo{" +
        "articleTitle='" + articleTitle + '\'' +
        ", articleName='" + articleName + '\'' +
        ", articleUrl='" + articleUrl + '\'' +
        ", langCode='" + langCode + '\'' +
        ", langName='" + langName + '\'' +
        ", langAutonym='" + langAutonym + '\'' +
        '}';
  }
}

