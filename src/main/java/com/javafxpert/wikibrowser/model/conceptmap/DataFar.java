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
  private GraphFar graphFar;

  public DataFar() {
  }

  public DataFar(GraphFar graphFar) {
    this.graphFar = graphFar;
  }

  public GraphFar getGraphFar() {
    return graphFar;
  }

  public void setGraphFar(GraphFar graphFar) {
    this.graphFar = graphFar;
  }

  @Override
  public String toString() {
    return "DataFar{" +
        "graphFar=" + graphFar +
        '}';
  }
}
