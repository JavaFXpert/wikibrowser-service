package com.javafxpert.wikibrowser.model.conceptmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultFar implements Serializable {
  @JsonProperty("data")
  private DataFar dataFar;

  public ResultFar() {
  }

  public ResultFar(DataFar dataFar) {
    this.dataFar = dataFar;
  }

  public DataFar getDataFar() {
    return dataFar;
  }

  public void setDataFar(DataFar dataFar) {
    this.dataFar = dataFar;
  }

  @Override
  public String toString() {
    return "ResultFar{" +
        "dataFar=" + dataFar +
        '}';
  }
}
