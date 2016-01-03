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
public class GraphNodeNear implements Serializable {
  @JsonProperty("id")
  private String id;

  @JsonProperty("title")
  private String title;

  @JsonProperty("labels")
  private List<String> labelsList = new ArrayList<>();

  public GraphNodeNear() {
  }

  public GraphNodeNear(String id, String title, List<String> labelsList) {
    this.id = id;
    this.title = title;
    this.labelsList = labelsList;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getLabelsList() {
    return labelsList;
  }

  public void setLabelsList(List<String> labelsList) {
    this.labelsList = labelsList;
  }

  @Override
  public String toString() {
    return "GraphNodeNear{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", labelsList=" + labelsList +
        '}';
  }
}
