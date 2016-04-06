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

package com.javafxpert.wikibrowser.model.traversalresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author James Weaver
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraversalBindingsFar {

  @JsonProperty("item")
  private ItemUrlFar itemUrlFar;

  @JsonProperty("itemLabel")
  private ItemLabelFar itemLabelFar;

  @JsonProperty("picture")
  private PictureFar pictureFar;

  public TraversalBindingsFar() {
  }

  public TraversalBindingsFar(ItemUrlFar itemUrlFar, ItemLabelFar itemLabelFar, PictureFar pictureFar) {
    this.itemUrlFar = itemUrlFar;
    this.itemLabelFar = itemLabelFar;
    this.pictureFar = pictureFar;
  }

  public ItemUrlFar getItemUrlFar() {
    return itemUrlFar;
  }

  public void setItemUrlFar(ItemUrlFar itemUrlFar) {
    this.itemUrlFar = itemUrlFar;
  }

  public ItemLabelFar getItemLabelFar() {
    return itemLabelFar;
  }

  public void setItemLabelFar(ItemLabelFar itemLabelFar) {
    this.itemLabelFar = itemLabelFar;
  }

  public PictureFar getPictureFar() {
    return pictureFar;
  }

  public void setPictureFar(PictureFar pictureFar) {
    this.pictureFar = pictureFar;
  }

  @Override
  public String toString() {
    return "TraversalBindingsFar{" +
        "itemUrlFar=" + itemUrlFar +
        ", itemLabelFar=" + itemLabelFar +
        ", pictureFar=" + pictureFar +
        '}';
  }
}


