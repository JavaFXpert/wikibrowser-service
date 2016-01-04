package com.javafxpert.wikibrowser.model.conceptmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphRelationPropsFar implements Serializable {
  @JsonProperty("propId")
  private String propId;

  @JsonProperty("label")
  private String label;

  public GraphRelationPropsFar() {
  }

  public GraphRelationPropsFar(String propId, String label) {
    this.propId = propId;
    this.label = label;
  }

  public String getPropId() {
    return propId;
  }

  public void setPropId(String propId) {
    this.propId = propId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return "GraphRelationPropsFar{" +
        "propId='" + propId + '\'' +
        ", label='" + label + '\'' +
        '}';
  }
}
