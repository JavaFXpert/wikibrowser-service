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
