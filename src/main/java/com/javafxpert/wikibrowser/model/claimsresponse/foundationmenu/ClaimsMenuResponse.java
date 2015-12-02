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

package com.javafxpert.wikibrowser.model.claimsresponse.foundationmenu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataClaim;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James Weaver
 */
@JsonRootName("itemClaims")
@JsonPropertyOrder({"lang", "wdItem", "wdItemBase", "wdPropBase", "articleTitle", "articleId", "wpBase",
                    "wpMobileBase", "claims"})
public class ClaimsMenuResponse {

  private String lang;
  private String wdItem;
  private String wdItemBase;
  private String wdPropBase;
  private String articleTitle;
  private String articleId;
  private String wpBase;
  private String wpMobileBase;

  @JacksonXmlElementWrapper(localName = "claims")
  @JsonProperty("claim")
  private List<WikidataClaim> claims = new ArrayList<>();

  public ClaimsMenuResponse(String lang, String wdItem, String wdItemBase, String wdPropBase, String articleTitle, String articleId, String wpMobileBase, String wpBase, List<WikidataClaim> claims) {
    this.lang = lang;
    this.wdItem = wdItem;
    this.wdItemBase = wdItemBase;
    this.wdPropBase = wdPropBase;
    this.articleTitle = articleTitle;
    this.articleId = articleId;
    this.wpMobileBase = wpMobileBase;
    this.wpBase = wpBase;
    this.claims = claims;
  }

  public ClaimsMenuResponse() {
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getWdItem() {
    return wdItem;
  }

  public void setWdItem(String wdItem) {
    this.wdItem = wdItem;
  }

  public String getWdItemBase() {
    return wdItemBase;
  }

  public void setWdItemBase(String wdItemBase) {
    this.wdItemBase = wdItemBase;
  }

  public String getWdPropBase() {
    return wdPropBase;
  }

  public void setWdPropBase(String wdPropBase) {
    this.wdPropBase = wdPropBase;
  }

  public String getArticleTitle() {
    return articleTitle;
  }

  public void setArticleTitle(String articleTitle) {
    this.articleTitle = articleTitle;
  }

  public String getArticleId() {
    return articleId;
  }

  public void setArticleId(String articleId) {
    this.articleId = articleId;
  }

  public String getWpBase() {
    return wpBase;
  }

  public void setWpBase(String wpBase) {
    this.wpBase = wpBase;
  }

  public String getWpMobileBase() {
    return wpMobileBase;
  }

  public void setWpMobileBase(String wpMobileBase) {
    this.wpMobileBase = wpMobileBase;
  }

  public List<WikidataClaim> getClaims() {
    return claims;
  }

  public void setClaims(List<WikidataClaim> claims) {
    this.claims = claims;
  }

  @Override
  public String toString() {
    return "ClaimsResponse{" +
        "lang='" + lang + '\'' +
        ", wdItem='" + wdItem + '\'' +
        ", wdItemBase='" + wdItemBase + '\'' +
        ", wdPropBase='" + wdPropBase + '\'' +
        ", articleTitle='" + articleTitle + '\'' +
        ", articleId='" + articleId + '\'' +
        ", wpBase='" + wpBase + '\'' +
        ", wpMobileBase='" + wpMobileBase + '\'' +
        ", claims=" + claims +
        '}';
  }
}

