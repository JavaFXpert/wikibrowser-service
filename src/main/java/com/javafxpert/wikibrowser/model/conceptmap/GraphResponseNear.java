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
