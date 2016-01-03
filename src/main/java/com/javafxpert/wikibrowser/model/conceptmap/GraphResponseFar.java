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
public class GraphResponseFar implements Serializable {
  @JsonProperty("results")
  private List<ResultFar> resultFarList = new ArrayList<>();

  public GraphResponseFar() {
  }

  public GraphResponseFar(List<ResultFar> resultFarList) {
    this.resultFarList = resultFarList;
  }

  public List<ResultFar> getResultFarList() {
    return resultFarList;
  }

  public void setResultFarList(List<ResultFar> resultFarList) {
    this.resultFarList = resultFarList;
  }

  @Override
  public String toString() {
    return "GraphResponseFar{" +
        "resultFarList=" + resultFarList +
        '}';
  }
}
