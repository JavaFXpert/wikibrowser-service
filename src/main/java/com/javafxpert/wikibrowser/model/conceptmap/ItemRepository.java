package com.javafxpert.wikibrowser.model.conceptmap;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

/**
 * Provides custom Cypher queries as repository search operations
 * Created by jamesweaver on 12/28/15.
 */
public interface ItemRepository extends GraphRepository<GraphItem> {

  /**
   * Adds an item to the repository
   * @param item
   */
  // MERGE (Q2:Item {itemId:"Q2", title:"Earth"})
  @Query("MERGE (:Item {itemId:{itemId}, title:{itemTitle}})")
  void addItem(@Param("itemId") String itemId, @Param("itemTitle") String itemTitle);
}
