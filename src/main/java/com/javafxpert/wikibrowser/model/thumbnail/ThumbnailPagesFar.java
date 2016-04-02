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

package com.javafxpert.wikibrowser.model.thumbnail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javafxpert.wikibrowser.model.idlocator.LangLinkFar;
import com.javafxpert.wikibrowser.model.idlocator.PagePropsFar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThumbnailPagesFar {
  @JsonProperty("title")
  private String title;

  @JsonProperty("thumbnail")
  private ThumbnailFar thumbnailFar;

  public ThumbnailPagesFar() {
  }

  public ThumbnailPagesFar(ThumbnailFar thumbnailFar, String title) {
    this.thumbnailFar = thumbnailFar;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ThumbnailFar getThumbnailFar() {
    return thumbnailFar;
  }

  public void setThumbnailFar(ThumbnailFar thumbnailFar) {
    this.thumbnailFar = thumbnailFar;
  }

  @Override
  public String toString() {
    return "ThumbnailPagesFar{" +
        "title='" + title + '\'' +
        ", thumbnailFar=" + thumbnailFar +
        '}';
  }
}


