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

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.io.Serializable;

/**
 * Represents a Neo4j node with an Item label
 * Created by jamesweaver on 12/28/15.
 */
@NodeEntity
public class GraphItem implements Serializable {

  @GraphId
  private Long id;

  /**
   * Item id from Wikidata (e.g. item id for "Earth" is "Q2"
   */
  @Index(unique = true)
  private String itemId;

  // TODO: Add @Relationship fields?

  /**
   * Title of item (e.g. English title of item "Q2" is "Earth"
   */
  private String title;

  public GraphItem() {
  }

  public GraphItem(Long id, String itemId, String title) {
    this.id = id;
    this.itemId = itemId;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    return "GraphItem{" +
        "id=" + id +
        ", itemId='" + itemId + '\'' +
        ", title='" + title + '\'' +
        '}';
  }
}
