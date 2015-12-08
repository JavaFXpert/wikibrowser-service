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
public class PagePropsFar {
  @JsonProperty("page_image")
  private String pageImage;

  @JsonProperty("wikibase_item")
  private String wikibaseItem;

  public PagePropsFar() {
  }

  public PagePropsFar(String pageImage, String wikibaseItem) {
    this.pageImage = pageImage;
    this.wikibaseItem = wikibaseItem;
  }

  public String getPageImage() {
    return pageImage;
  }

  public void setPageImage(String pageImage) {
    this.pageImage = pageImage;
  }

  public String getWikibaseItem() {
    return wikibaseItem;
  }

  public void setWikibaseItem(String wikibaseItem) {
    this.wikibaseItem = wikibaseItem;
  }

  @Override
  public String toString() {
    return "PagePropsFar{" +
        "pageImage='" + pageImage + '\'' +
        ", wikibaseItem='" + wikibaseItem + '\'' +
        '}';
  }
}


