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
package com.javafxpert.wikibrowser.model.visgraph;

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
@JsonPropertyOrder({"nodes", "edges"})

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisGraphResponseNear implements Serializable {
  @JsonProperty("nodes")
  private List<VisGraphNodeNear> visGraphNodeNearList = new ArrayList<>();

  @JsonProperty("edges")
  private List<VisGraphEdgeNear> visGraphEdgeNearList = new ArrayList<>();

  public VisGraphResponseNear() {
  }

  public VisGraphResponseNear(List<VisGraphNodeNear> visGraphNodeNearList, List<VisGraphEdgeNear> visGraphEdgeNearList) {
    this.visGraphNodeNearList = visGraphNodeNearList;
    this.visGraphEdgeNearList = visGraphEdgeNearList;
  }

  public List<VisGraphNodeNear> getVisGraphNodeNearList() {
    return visGraphNodeNearList;
  }

  public void setVisGraphNodeNearList(List<VisGraphNodeNear> visGraphNodeNearList) {
    this.visGraphNodeNearList = visGraphNodeNearList;
  }

  public List<VisGraphEdgeNear> getVisGraphEdgeNearList() {
    return visGraphEdgeNearList;
  }

  public void setVisGraphEdgeNearList(List<VisGraphEdgeNear> visGraphEdgeNearList) {
    this.visGraphEdgeNearList = visGraphEdgeNearList;
  }

  @Override
  public String toString() {
    return "VisGraphResponseNear{" +
        "visGraphNodeNearList=" + visGraphNodeNearList +
        ", visGraphEdgeNearList=" + visGraphEdgeNearList +
        '}';
  }
}
