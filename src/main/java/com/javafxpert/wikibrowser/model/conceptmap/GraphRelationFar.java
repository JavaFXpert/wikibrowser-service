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
public class GraphRelationFar implements Serializable {
  @JsonProperty("id")
  private String id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("startNode")
  private String startNode;

  @JsonProperty("endNode")
  private String endNode;

  @JsonProperty("properties")
  private GraphRelationPropsFar graphRelationPropsFar;

  public GraphRelationFar() {
  }

  public GraphRelationFar(String id, String type, String startNode, String endNode, GraphRelationPropsFar graphRelationPropsFar) {
    this.id = id;
    this.type = type;
    this.startNode = startNode;
    this.endNode = endNode;
    this.graphRelationPropsFar = graphRelationPropsFar;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStartNode() {
    return startNode;
  }

  public void setStartNode(String startNode) {
    this.startNode = startNode;
  }

  public String getEndNode() {
    return endNode;
  }

  public void setEndNode(String endNode) {
    this.endNode = endNode;
  }

  public GraphRelationPropsFar getGraphRelationPropsFar() {
    return graphRelationPropsFar;
  }

  public void setGraphRelationPropsFar(GraphRelationPropsFar graphRelationPropsFar) {
    this.graphRelationPropsFar = graphRelationPropsFar;
  }

  @Override
  public String toString() {
    return "GraphRelationFar{" +
        "id='" + id + '\'' +
        ", type='" + type + '\'' +
        ", startNode='" + startNode + '\'' +
        ", endNode='" + endNode + '\'' +
        ", graphRelationPropsFar=" + graphRelationPropsFar +
        '}';
  }
}
