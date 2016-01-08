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
package com.javafxpert.wikibrowser.model.conceptmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphFar implements Serializable {
  @JsonProperty("nodes")
  private List<GraphNodeFar> graphNodeFarList = new ArrayList<>();

  @JsonProperty("relationships")
  private List<GraphRelationFar> graphRelationFarList = new ArrayList<>();

  public GraphFar() {
  }

  public GraphFar(List<GraphNodeFar> graphNodeFarList, List<GraphRelationFar> graphRelationFarList) {
    this.graphNodeFarList = graphNodeFarList;
    this.graphRelationFarList = graphRelationFarList;
  }

  public List<GraphNodeFar> getGraphNodeFarList() {
    return graphNodeFarList;
  }

  public void setGraphNodeFarList(List<GraphNodeFar> graphNodeFarList) {
    this.graphNodeFarList = graphNodeFarList;
  }

  public List<GraphRelationFar> getGraphRelationFarList() {
    return graphRelationFarList;
  }

  public void setGraphRelationFarList(List<GraphRelationFar> graphRelationFarList) {
    this.graphRelationFarList = graphRelationFarList;
  }

  @Override
  public String toString() {
    return "GraphFar{" +
        "graphNodeFarList=" + graphNodeFarList +
        ", graphRelationFarList=" + graphRelationFarList +
        '}';
  }
}
