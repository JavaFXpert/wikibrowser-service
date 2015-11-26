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

package com.javafxpert.wikibrowser.model.claimsresponse;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

/**
 * Created by jamesweaver on 11/25/15.
 */
@JsonRootName("val")
public class WikidataItem implements Serializable {
  private String id;
  private String label;

  public WikidataItem(String id, String label) {
    this.id = id;
    this.label = label;
  }

  public WikidataItem() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return "WikidataItem{" +
        "id='" + id + '\'' +
        ", label='" + label + '\'' +
        '}';
  }
}
