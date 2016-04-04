/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javafxpert.wikibrowser.model.visgraph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonPropertyOrder({"id", "label", "image", "types", "itemId"})

//@JsonIgnoreProperties(ignoreUnknown = true)
public class VisGraphNodeNear implements Serializable {
  @JsonProperty("id")
  private String dbId;  // Node ID from the database

  @JsonProperty("label")
  private String title;

  @JsonProperty("image")
  private String imageUrl;

  @JsonProperty("types")
  private List<String> labelsList = new ArrayList<>();

  @JsonProperty("itemId")
  private String itemId;  // Wikidata item ID (begins with a Q)

  public VisGraphNodeNear() {
  }

  public VisGraphNodeNear(String dbId, String title, String imageUrl, List<String> labelsList, String itemId) {
    this.dbId = dbId;
    this.title = title;
    this.imageUrl = imageUrl;
    this.labelsList = labelsList;
    this.itemId = itemId;
  }

  public String getDbId() {
    return dbId;
  }

  public void setDbId(String dbId) {
    this.dbId = dbId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<String> getLabelsList() {
    return labelsList;
  }

  public void setLabelsList(List<String> labelsList) {
    this.labelsList = labelsList;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  @Override
  public String toString() {
    return "VisGraphNodeNear{" +
        "dbId='" + dbId + '\'' +
        ", title='" + title + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", labelsList=" + labelsList +
        ", itemId='" + itemId + '\'' +
        '}';
  }
}
