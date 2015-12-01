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

package com.javafxpert.wikibrowser.model.locator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataClaim;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James Weaver
 *
 * TODO: Remove some fields when more is known about which ones will be utilized by clients
 */
@JsonRootName("itemInfo")
@JsonPropertyOrder({"articleName", "site", "articleUrl", "itemId", "lang"})
public class ItemInfo {

  private String articleName;
  private String site;
  private String articleUrl;
  private String itemId;
  private String lang;

  public ItemInfo(String articleName, String site, String articleUrl, String itemId, String lang) {
    this.articleName = articleName;
    this.site = site;
    this.articleUrl = articleUrl;
    this.itemId = itemId;
    this.lang = lang;
  }

  public ItemInfo() {
  }

  public String getArticleName() {
    return articleName;
  }

  public void setArticleName(String articleName) {
    this.articleName = articleName;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getArticleUrl() {
    return articleUrl;
  }

  public void setArticleUrl(String articleUrl) {
    this.articleUrl = articleUrl;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  @Override
  public String toString() {
    return "ItemInfo{" +
        "articleName='" + articleName + '\'' +
        ", site='" + site + '\'' +
        ", articleUrl='" + articleUrl + '\'' +
        ", itemId='" + itemId + '\'' +
        ", lang='" + lang + '\'' +
        '}';
  }
}

