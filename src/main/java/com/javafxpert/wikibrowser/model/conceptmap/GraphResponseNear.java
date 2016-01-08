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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonRootName("graph")
@JsonPropertyOrder({"nodes", "links"})

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphResponseNear implements Serializable {
  @JsonProperty("nodes")
  private List<GraphNodeNear> graphNodeNearList = new ArrayList<>();

  @JsonProperty("links")
  private List<GraphLinkNear> graphLinkNearList = new ArrayList<>();

  public GraphResponseNear() {
  }

  public GraphResponseNear(List<GraphNodeNear> graphNodeNearList, List<GraphLinkNear> graphLinkNearList) {
    this.graphNodeNearList = graphNodeNearList;
    this.graphLinkNearList = graphLinkNearList;
  }

  public List<GraphNodeNear> getGraphNodeNearList() {
    return graphNodeNearList;
  }

  public void setGraphNodeNearList(List<GraphNodeNear> graphNodeNearList) {
    this.graphNodeNearList = graphNodeNearList;
  }

  public List<GraphLinkNear> getGraphLinkNearList() {
    return graphLinkNearList;
  }

  public void setGraphLinkNearList(List<GraphLinkNear> graphLinkNearList) {
    this.graphLinkNearList = graphLinkNearList;
  }

  @Override
  public String toString() {
    return "GraphResponseNear{" +
        "graphNodeNearList=" + graphNodeNearList +
        ", graphLinkNearList=" + graphLinkNearList +
        '}';
  }
}
