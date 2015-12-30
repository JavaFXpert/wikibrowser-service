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
      System.out.println("\n\nNeed to create method " + methodStr + "() in ItemRepository\n\n");
      // TODO: Remove println above and decide how to report
      addRel(itemIdA, itemIdB, propId, propLabel);
    }
  }

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INSTANCE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP31(@Param("itemIdA") String itemIdA,
                 @Param("itemIdB") String itemIdB,
                 @Param("propId") String propId,
                 @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DISCOVERER_OR_INVENTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP61(@Param("itemIdA") String itemIdA,
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

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FOLLOWS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP155(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FOLLOWED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP156(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SUBCLASS_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP279(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_MAIN_TOPIC {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP301(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:IS_A_LIST_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP360(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PART_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP361(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:USE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP366(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ASTRONOMICAL_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP397(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CHILD_ASTRONOMICAL_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP398(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:QUANTITY_SYMBOL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP416(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SAID_TO_BE_THE_SAME_AS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP460(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OPPOSITE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP461(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COLOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP462(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SRGB_COLOR_HEX_TRIPLET {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP465(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ARCHIVES_AT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP485(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:UNICODE_CHARACTER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP487(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPLIES_TO_PART {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP518(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TYPE_OF_ORBIT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP522(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_PART {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP527(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TIME_OF_DISCOVERY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP575(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HIGHEST_POINT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP610(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATED_ON_TERRAIN_FEATURE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP706(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPROVED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP790(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TOPIC_MAIN_CATEGORY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP910(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NOTATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP913(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PATENT_NUMBER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1246(@Param("itemIdA") String itemIdA,
                  @Param("itemIdB") String itemIdB,
                  @Param("propId") String propId,
                  @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FACET_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1269(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PROVED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1318(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DESCRIBED_BY_SOURCE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1343(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SHAPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1419(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SOURCING_CIRCUMSTANCES {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1480(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NAME_IN_KANA {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1814(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DIFFERENT_FROM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1889(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HISTORY_OF_TOPIC {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2184(@Param("itemIdA") String itemIdA,
                   @Param("itemIdB") String itemIdB,
                   @Param("propId") String propId,
                   @Param("propLabel") String propLabel);




  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:r {propId:{propId}, label:{propLabel}}]->(b)")
  void addRel(@Param("itemIdA") String itemIdA,
              @Param("itemIdB") String itemIdB,
              @Param("propId") String propId,
              @Param("propLabel") String propLabel);

}
