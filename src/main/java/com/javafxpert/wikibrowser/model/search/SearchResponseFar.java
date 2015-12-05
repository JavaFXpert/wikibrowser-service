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

package com.javafxpert.wikibrowser.model.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponseFar {
  @JsonProperty("query")
  private QueryFar queryFar;

  public SearchResponseFar(QueryFar queryFar) {
    this.queryFar = queryFar;
  }

  public SearchResponseFar() {
  }

  public QueryFar getQueryFar() {
    return queryFar;
  }

  public void setQueryFar(QueryFar queryFar) {
    this.queryFar = queryFar;
  }

  @Override
  public String toString() {
    return "SearchResponseFar{" +
        "queryFar=" + queryFar +
        '}';
  }
}

