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
import com.javafxpert.wikibrowser.model.idlocator.PagesFar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphNodeFar implements Serializable {
  @JsonProperty("id")
  private String id;

  @JsonProperty("labels")
  private List<String> labelsList = new ArrayList<>();

  @JsonProperty("properties")
  private GraphNodePropsFar graphNodePropsFar;

  public GraphNodeFar() {
  }

  public GraphNodeFar(String id, List<String> labelsList, GraphNodePropsFar graphNodePropsFar) {
    this.id = id;
    this.labelsList = labelsList;
    this.graphNodePropsFar = graphNodePropsFar;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getLabelsList() {
    return labelsList;
  }

  public void setLabelsList(List<String> labelsList) {
    this.labelsList = labelsList;
  }

  public GraphNodePropsFar getGraphNodePropsFar() {
    return graphNodePropsFar;
  }

  public void setGraphNodePropsFar(GraphNodePropsFar graphNodePropsFar) {
    this.graphNodePropsFar = graphNodePropsFar;
  }

  @Override
  public String toString() {
    return "GraphNodeFar{" +
        "id='" + id + '\'' +
        ", labelsList=" + labelsList +
        ", graphNodePropsFar=" + graphNodePropsFar +
        '}';
  }
}
