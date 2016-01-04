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
public class GraphRelationFar implements Serializable {
  @JsonProperty("id")
  private String id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("startNode")
  private String startNode;

  @JsonProperty("endNode")
  private String endNode;

  @JsonProperty("properties")
  private GraphRelationPropsFar graphRelationPropsFar;

  public GraphRelationFar() {
  }

  public GraphRelationFar(String id, String type, String startNode, String endNode, GraphRelationPropsFar graphRelationPropsFar) {
    this.id = id;
    this.type = type;
    this.startNode = startNode;
    this.endNode = endNode;
    this.graphRelationPropsFar = graphRelationPropsFar;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStartNode() {
    return startNode;
  }

  public void setStartNode(String startNode) {
    this.startNode = startNode;
  }

  public String getEndNode() {
    return endNode;
  }

  public void setEndNode(String endNode) {
    this.endNode = endNode;
  }

  public GraphRelationPropsFar getGraphRelationPropsFar() {
    return graphRelationPropsFar;
  }

  public void setGraphRelationPropsFar(GraphRelationPropsFar graphRelationPropsFar) {
    this.graphRelationPropsFar = graphRelationPropsFar;
  }

  @Override
  public String toString() {
    return "GraphRelationFar{" +
        "id='" + id + '\'' +
        ", type='" + type + '\'' +
        ", startNode='" + startNode + '\'' +
        ", endNode='" + endNode + '\'' +
        ", graphRelationPropsFar=" + graphRelationPropsFar +
        '}';
  }
}
