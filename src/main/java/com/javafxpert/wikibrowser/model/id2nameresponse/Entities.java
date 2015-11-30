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
import com.sun.javafx.collections.MappingChange;

import java.util.Map;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entities {
  private Map<String, Item> itemMap;

  public Entities() {
  }

  public Map<String, Item> getItemMap() {
    return itemMap;
  }

  public void setItemMap(Map<String, Item> itemMap) {
    this.itemMap = itemMap;
  }

  @Override
  public String toString() {
    return "Entities{" +
        "itemMap=" + itemMap +
        '}';
  }
}


