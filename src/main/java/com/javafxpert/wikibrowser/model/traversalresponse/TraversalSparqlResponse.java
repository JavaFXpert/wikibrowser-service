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
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Head;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Results;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraversalSparqlResponse {
  private Head head;

  @JsonProperty("results")
  private TraversalResultsFar traversalResultsFar;

  public TraversalSparqlResponse() {
  }

  public TraversalSparqlResponse(Head head, TraversalResultsFar traversalResultsFar) {
    this.head = head;
    this.traversalResultsFar = traversalResultsFar;
  }

  public Head getHead() {
    return head;
  }

  public void setHead(Head head) {
    this.head = head;
  }

  public TraversalResultsFar getTraversalResultsFar() {
    return traversalResultsFar;
  }

  public void setTraversalResultsFar(TraversalResultsFar traversalResultsFar) {
    this.traversalResultsFar = traversalResultsFar;
  }

  @Override
  public String toString() {
    return "TraversalSparqlResponse{" +
        "head=" + head +
        ", traversalResultsFar=" + traversalResultsFar +
        '}';
  }
}

