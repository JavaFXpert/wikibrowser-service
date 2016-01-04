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

  public GraphFar() {
  }

  public GraphFar(List<GraphNodeFar> graphNodeFarList) {
    this.graphNodeFarList = graphNodeFarList;
  }

  public List<GraphNodeFar> getGraphNodeFarList() {
    return graphNodeFarList;
  }

  public void setGraphNodeFarList(List<GraphNodeFar> graphNodeFarList) {
    this.graphNodeFarList = graphNodeFarList;
  }

  @Override
  public String toString() {
    return "GraphFar{" +
        "graphNodeFarList=" + graphNodeFarList +
        '}';
  }
}
