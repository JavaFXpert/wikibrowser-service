package com.javafxpert.wikibrowser.model.conceptmap;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Provides custom Cypher queries as repository search operations
 * Created by jamesweaver on 12/28/15.
 */
public interface ItemRepository extends GraphRepository<GraphItem> {

  /**
   * Adds an item to the repository
   * @param itemId
   * @param itemTitle
   */
  // MERGE (Q2:Item {itemId:"Q2", title:"Earth"})
  @Query("MERGE (:Item {itemId:{itemId}, title:{itemTitle}})")
  void addItem(@Param("itemId") String itemId, @Param("itemTitle") String itemTitle);

  /**
   * Adds an relationship to the repository
   * @param itemIdA
   * @param itemIdB
   * @param propId
   * @param propLabel
   *
   * TODO: Consider using reflection for method invocation
   * TODO: Protect against null or empty arguments
   */
  // MATCH (a:Item {itemId:"Q2"}), (b:Item {itemId:"Q185969"})
  // MERGE (a)-[:SHAPE {propId:"P1419", label:"shape"}]->(b)
  default void addRelationship(String itemIdA,
              String itemIdB,
              String propId,
              String propLabel) {

    String methodStr = "addRel" + propId;
    Method method = ReflectionUtils.findMethod(ItemRepository.class, methodStr, new Class[]{String.class, String.class, String.class, String.class});
    if (method != null) {
      try {
        ReflectionUtils.invokeMethod(method, this, itemIdA, itemIdB, propId, propLabel);
      }
      catch (Exception e) {
        System.out.println("Exception in invokeMethod " + methodStr + ": " + e);
        // TODO: Remove println above and decide how to report
      }
    }
    else {
      addRel(itemIdA, itemIdB, propId, propLabel);
    }
  }

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INSTANCE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP31(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OWNED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP127(@Param("itemIdA") String itemIdA,
                 @Param("itemIdB") String itemIdB,
                 @Param("propId") String propId,
                 @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NAMED_AFTER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP138(@Param("itemIdA") String itemIdA,
                 @Param("itemIdB") String itemIdB,
                 @Param("propId") String propId,
                 @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SUBCLASS_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP279(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CAT_MAIN_TOPIC {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP301(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PART_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP361(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_PART {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP527(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPROVED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP790(@Param("itemIdA") String itemIdA,
                 @Param("itemIdB") String itemIdB,
                 @Param("propId") String propId,
                 @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:R {propId:{propId}, label:{propLabel}}]->(b)")
  void addRel(@Param("itemIdA") String itemIdA,
              @Param("itemIdB") String itemIdB,
              @Param("propId") String propId,
              @Param("propLabel") String propLabel);

}
