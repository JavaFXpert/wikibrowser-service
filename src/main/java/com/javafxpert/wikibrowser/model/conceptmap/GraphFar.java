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
