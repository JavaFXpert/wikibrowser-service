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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonPropertyOrder({"from", "to", "label", "arrows", "propId", "fromItemId", "toItemId"})

public class VisGraphEdgeNear implements Serializable {
  @JsonProperty("from")
  private String fromDbId;

  @JsonProperty("to")
  private String toDbId;

  @JsonProperty("label")
  private String label;

  @JsonProperty("arrows")
  private String arrowDirection;

  @JsonProperty("propId")
  private String propId;

  @JsonProperty("fromItemId")
  private String fromItemId;

  @JsonProperty("toItemId")
  private String toItemId;

  public VisGraphEdgeNear() {
  }

  public VisGraphEdgeNear(String fromDbId, String toDbId, String label, String arrowDirection, String propId, String fromItemId, String toItemId) {
    this.fromDbId = fromDbId;
    this.toDbId = toDbId;
    this.label = label;
    this.arrowDirection = arrowDirection;
    this.propId = propId;
    this.fromItemId = fromItemId;
    this.toItemId = toItemId;
  }

  public String getFromDbId() {
    return fromDbId;
  }

  public void setFromDbId(String fromDbId) {
    this.fromDbId = fromDbId;
  }

  public String getToDbId() {
    return toDbId;
  }

  public void setToDbId(String toDbId) {
    this.toDbId = toDbId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getArrowDirection() {
    return arrowDirection;
  }

  public void setArrowDirection(String arrowDirection) {
    this.arrowDirection = arrowDirection;
  }

  public String getPropId() {
    return propId;
  }

  public void setPropId(String propId) {
    this.propId = propId;
  }

  public String getFromItemId() {
    return fromItemId;
  }

  public void setFromItemId(String fromItemId) {
    this.fromItemId = fromItemId;
  }

  public String getToItemId() {
    return toItemId;
  }

  public void setToItemId(String toItemId) {
    this.toItemId = toItemId;
  }

  @Override
  public String toString() {
    return "VisGraphEdgeNear{" +
        "fromDbId='" + fromDbId + '\'' +
        ", toDbId='" + toDbId + '\'' +
        ", label='" + label + '\'' +
        ", arrowDirection='" + arrowDirection + '\'' +
        ", propId='" + propId + '\'' +
        ", fromItemId='" + fromItemId + '\'' +
        ", toItemId='" + toItemId + '\'' +
        '}';
  }
}
