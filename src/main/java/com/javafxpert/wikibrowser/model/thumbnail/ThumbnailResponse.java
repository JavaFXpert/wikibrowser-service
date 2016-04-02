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
import com.javafxpert.wikibrowser.model.idlocator.IdQueryFar;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThumbnailResponse {
  @JsonProperty("query")
  private ThumbnailQueryFar thumbnailQueryFar;

  public ThumbnailResponse() {
  }

  public ThumbnailResponse(ThumbnailQueryFar thumbnailQueryFar) {
    this.thumbnailQueryFar = thumbnailQueryFar;
  }

  public ThumbnailQueryFar getThumbnailQueryFar() {
    return thumbnailQueryFar;
  }

  public void setThumbnailQueryFar(ThumbnailQueryFar thumbnailQueryFar) {
    this.thumbnailQueryFar = thumbnailQueryFar;
  }

  @Override
  public String toString() {
    return "ThumbnailResponse{" +
        "thumbnailQueryFar=" + thumbnailQueryFar +
        '}';
  }
}

