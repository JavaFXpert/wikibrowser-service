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
