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
public class DataFar implements Serializable {
  @JsonProperty("graph")
  private List<GraphFar> graphFarList = new ArrayList<>();

  public DataFar() {
  }

  public DataFar(List<GraphFar> graphFarList) {
    this.graphFarList = graphFarList;
  }

  public List<GraphFar> getGraphFarList() {
    return graphFarList;
  }

  public void setGraphFarList(List<GraphFar> graphFarList) {
    this.graphFarList = graphFarList;
  }

  @Override
  public String toString() {
    return "DataFar{" +
        "graphFarList=" + graphFarList +
        '}';
  }
}
