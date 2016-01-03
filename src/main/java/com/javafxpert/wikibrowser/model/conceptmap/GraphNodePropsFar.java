package com.javafxpert.wikibrowser.model.conceptmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphNodePropsFar implements Serializable {
  private String title;
  private String itemId;

  public GraphNodePropsFar() {
  }

  public GraphNodePropsFar(String itemId, String title) {
    this.itemId = itemId;
    this.title = title;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "GraphNodePropsFar{" +
        "title='" + title + '\'' +
        ", itemId='" + itemId + '\'' +
        '}';
  }
}
