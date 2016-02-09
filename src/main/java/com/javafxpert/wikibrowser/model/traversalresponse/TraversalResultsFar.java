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

package com.javafxpert.wikibrowser.model.traversalresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Bindings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraversalResultsFar {
  private List<TraversalBindingsFar> traversalBindingsFarList = new ArrayList<>();

  public TraversalResultsFar() {
  }

  @JsonProperty("bindings")
  public List<TraversalBindingsFar> getTraversalBindingsFarList() {
    return traversalBindingsFarList;
  }

  public void setTraversalBindingsFarList(List<TraversalBindingsFar> traversalBindingsFarList) {
    this.traversalBindingsFarList = traversalBindingsFarList;
  }

  public TraversalResultsFar(List<TraversalBindingsFar> traversalBindingsFarList) {

    this.traversalBindingsFarList = traversalBindingsFarList;
  }

  @Override
  public String toString() {
    return "TraversalResultsFar{" +
        "traversalBindingsFarList=" + traversalBindingsFarList +
        '}';
  }
}


