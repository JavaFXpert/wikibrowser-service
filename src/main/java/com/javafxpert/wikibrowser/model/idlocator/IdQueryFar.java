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
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Bindings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdQueryFar {
  @JsonProperty("pages")
  private List<PagesFar> pagesFarList = new ArrayList<>();

  public IdQueryFar() {
  }

  public IdQueryFar(List<PagesFar> pagesFarList) {
    this.pagesFarList = pagesFarList;
  }

  public List<PagesFar> getPagesFarList() {
    return pagesFarList;
  }

  public void setPagesFarList(List<PagesFar> pagesFarList) {
    this.pagesFarList = pagesFarList;
  }

  @Override
  public String toString() {
    return "IdQueryFar{" +
        "pagesFarList=" + pagesFarList +
        '}';
  }
}


