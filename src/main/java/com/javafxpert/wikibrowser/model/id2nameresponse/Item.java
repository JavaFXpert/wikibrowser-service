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

package com.javafxpert.wikibrowser.model.id2nameresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.PropLabel;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.PropUrl;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.ValLabel;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.ValUrl;

import java.util.Map;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
  private String type;
  private String id;
  private Map<String, Sitelinks> sitelinks;

  public Item() {
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, Sitelinks> getSitelinks() {
    return sitelinks;
  }

  public void setSitelinks(Map<String, Sitelinks> sitelinks) {
    this.sitelinks = sitelinks;
  }

  @Override
  public String toString() {
    return "Item{" +
        "type='" + type + '\'' +
        ", id='" + id + '\'' +
        ", sitelinks=" + sitelinks +
        '}';
  }
}


