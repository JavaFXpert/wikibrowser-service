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
import com.javafxpert.wikibrowser.model.claimssparqlresponse.PropLabel;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.PropUrl;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.ValLabel;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.ValUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagesFar {
  @JsonProperty("title")
  private String title;

  @JsonProperty("pagelanguage")
  private String pageLanguage;

  @JsonProperty("fullurl")
  private String fullUrl;

  @JsonProperty("pageprops")
  private PagePropsFar pagePropsFar;

  @JsonProperty("langlinks")
  private List<LangLinkFar> langLinkFarList = new ArrayList<>();

  public PagesFar() {
  }

  public PagesFar(String title, String pageLanguage, String fullUrl, PagePropsFar pagePropsFar, List<LangLinkFar> langLinkFarList) {
    this.title = title;
    this.pageLanguage = pageLanguage;
    this.fullUrl = fullUrl;
    this.pagePropsFar = pagePropsFar;
    this.langLinkFarList = langLinkFarList;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPageLanguage() {
    return pageLanguage;
  }

  public void setPageLanguage(String pageLanguage) {
    this.pageLanguage = pageLanguage;
  }

  public String getFullUrl() {
    return fullUrl;
  }

  public void setFullUrl(String fullUrl) {
    this.fullUrl = fullUrl;
  }

  public PagePropsFar getPagePropsFar() {
    return pagePropsFar;
  }

  public void setPagePropsFar(PagePropsFar pagePropsFar) {
    this.pagePropsFar = pagePropsFar;
  }

  public List<LangLinkFar> getLangLinkFarList() {
    return langLinkFarList;
  }

  public void setLangLinkFarList(List<LangLinkFar> langLinkFarList) {
    this.langLinkFarList = langLinkFarList;
  }

  @Override
  public String toString() {
    return "PagesFar{" +
        "title='" + title + '\'' +
        ", pageLanguage='" + pageLanguage + '\'' +
        ", fullUrl='" + fullUrl + '\'' +
        ", pagePropsFar=" + pagePropsFar +
        ", langLinkFarList=" + langLinkFarList +
        '}';
  }
}


