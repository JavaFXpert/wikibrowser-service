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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 11/25/15.
 */
public class WikidataClaim implements Serializable {
  @JsonProperty("prop")
  private WikidataProperty prop;

  @JacksonXmlElementWrapper(localName = "vals")
  @JsonProperty("val")
  private List<WikidataItem> values = new ArrayList<>();

  public WikidataClaim(WikidataProperty url, List<WikidataItem> values) {
    this.prop = prop;
    this.values = values;
  }

  public WikidataClaim() {
  }

  public WikidataProperty getProp() {
    return prop;
  }

  public void setProp(WikidataProperty prop) {
    this.prop = prop;
  }

  public List<WikidataItem> getValues() {
    return values;
  }

  public void setValues(List<WikidataItem> values) {
    this.values = values;
  }

  public void addItem(WikidataItem item) {
    this.values.add(item);
  }

  @Override
  public String toString() {
    return "WikidataClaim{" +
        "prop=" + prop +
        ", values=" + values +
        '}';
  }
}
