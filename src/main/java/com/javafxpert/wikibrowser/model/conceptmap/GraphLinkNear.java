package com.javafxpert.wikibrowser.model.conceptmap;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by jamesweaver on 1/2/16.
 */
public class GraphLinkNear implements Serializable {
  @JsonProperty("source")
  private String source;

  @JsonProperty("target")
  private String target;

  @JsonProperty("propId")
  private String propId;

  @JsonProperty("label")
  private String label;

  @JsonProperty("type")
  private String type;

  public GraphLinkNear() {
  }

  public GraphLinkNear(String source, String target, String propId, String label, String type) {
    this.source = source;
    this.target = target;
    this.propId = propId;
    this.label = label;
    this.type = type;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "GraphLinkNear{" +
        "source='" + source + '\'' +
        ", target='" + target + '\'' +
        ", propId='" + propId + '\'' +
        ", label='" + label + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
