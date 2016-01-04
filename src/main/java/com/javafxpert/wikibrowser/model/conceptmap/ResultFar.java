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
public class ResultFar implements Serializable {
  @JsonProperty("data")
  private List<DataFar> dataFarList = new ArrayList<>();

  public ResultFar() {
  }

  public ResultFar(List<DataFar> dataFarList) {
    this.dataFarList = dataFarList;
  }

  public List<DataFar> getDataFarList() {
    return dataFarList;
  }

  public void setDataFarList(List<DataFar> dataFarList) {
    this.dataFarList = dataFarList;
  }

  @Override
  public String toString() {
    return "ResultFar{" +
        "dataFarList=" + dataFarList +
        '}';
  }
}
