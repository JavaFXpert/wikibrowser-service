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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 11/25/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryFar {
  @JsonProperty("searchinfo")
  private SearchinfoFar searchinfoFar;

  @JsonProperty("search")
  private List<SearchFar> searchFar = new ArrayList<>();

  public QueryFar(SearchinfoFar searchinfoFar, List<SearchFar> searchFar) {
    this.searchinfoFar = searchinfoFar;
    this.searchFar = searchFar;
  }

  public QueryFar() {
  }

  public SearchinfoFar getSearchinfoFar() {
    return searchinfoFar;
  }

  public void setSearchinfoFar(SearchinfoFar searchinfoFar) {
    this.searchinfoFar = searchinfoFar;
  }

  public List<SearchFar> getSearchFar() {
    return searchFar;
  }

  public void setSearchFar(List<SearchFar> searchFar) {
    this.searchFar = searchFar;
  }

  @Override
  public String toString() {
    return "QueryFar{" +
        "searchinfoFar=" + searchinfoFar +
        ", searchFar=" + searchFar +
        '}';
  }
}
