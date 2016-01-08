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
  //@Query("MERGE (:Item {itemId:{itemId}, title:{itemTitle}})")
  @Query("MERGE (a:Item {itemId:{itemId}}) SET a.title = {itemTitle}")
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
      System.out.println("\n\nNeed to create method " + methodStr + "() in ItemRepository:\n\n");

      String capsPropLabel = propLabel.trim().replaceAll(" ", "_").toUpperCase();
      System.out.println("  @Query(\"MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:" +
          capsPropLabel + " {propId:{propId}, label:{propLabel}}]->(b)\")"
      );
      System.out.println("  void " + methodStr +
      "(@Param(\"itemIdA\") String itemIdA, @Param(\"itemIdB\") String itemIdB, @Param(\"propId\") String propId, @Param(\"propLabel\") String propLabel);"
      );

      System.out.println("\n\n");

      // TODO: Remove println above and decide how to report
      addRel(itemIdA, itemIdB, propId, propLabel);
    }
  }

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HEAD_OF_GOVERNMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP6(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BROTHER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP7(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SISTER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP9(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COUNTRY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP17(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PLACE_OF_BIRTH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP19(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PLACE_OF_DEATH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP20(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SEX_OR_GENDER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP21(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FATHER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP22(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MOTHER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP25(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SPOUSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP26(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COUNTRY_OF_CITIZENSHIP {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP27(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONTINENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP30(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INSTANCE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP31(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HEAD_OF_STATE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP35(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CAPITAL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP36(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OFFICIAL_LANGUAGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP37(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CURRENCY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP38(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:POSITION_HELD {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP39(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CHILD {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP40(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:STEPFATHER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP43(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SHARES_BORDER_WITH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP47(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:AUTHOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP50(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NOBLE_FAMILY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP53(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DISCOVERER_OR_INVENTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP61(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SITE_OF_ASTRONOMICAL_DISCOVERY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP65(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EDUCATED_AT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP69(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TOP_LEVEL_DOMAIN {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP78(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ANTHEM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP85(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FIELD_OF_WORK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP101(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MEMBER_OF_POLITICAL_PARTY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP102(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NATIVE_LANGUAGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP103(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OCCUPATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP106(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EMPLOYER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP108(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PLACE_OF_BURIAL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP119(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BASIC_FORM_OF_GOVERNMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP122(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PUBLISHER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP123(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OWNED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP127(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATED_IN_THE_ADMINISTRATIVE_TERRITORIAL_ENTITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP131(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:GENRE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP136(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NAMED_AFTER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP138(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RELIGION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP140(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONTAINS_ADMINISTRATIVE_TERRITORIAL_ENTITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP150(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FOLLOWS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP155(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FOLLOWED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP156(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FLAG {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP163(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:AWARD_RECEIVED {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP166(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ETHNIC_GROUP {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP172(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PERFORMER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP175(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEVELOPER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP178(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SERIES {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP179(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SISTER_CITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP190(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LEGISLATIVE_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP194(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATED_NEXT_TO_BODY_OF_WATER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP206(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EXECUTIVE_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP208(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HIGHEST_JUDICIAL_AUTHORITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP209(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COAT_OF_ARMS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP237(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MILITARY_BRANCH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP241(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RECORD_LABEL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP264(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LICENSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP275(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PROGRAMMING_LANGUAGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP277(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SUBCLASS_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP279(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:WRITING_SYSTEM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP282(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PLACE_OF_PUBLICATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP291(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_MAIN_TOPIC {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP301(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OPERATING_SYSTEM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP306(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:IS_A_LIST_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP360(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PART_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP361(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ORIGINAL_LANGUAGE_OF_WORK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP364(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:USE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP366(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ASTRONOMICAL_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP397(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CHILD_ASTRONOMICAL_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP398(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MILITARY_RANK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP410(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CANONIZATION_STATUS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP411(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:QUANTITY_SYMBOL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP416(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PATRON_SAINT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP417(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SEAL_DESCRIPTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP418(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATED_IN_TIME_ZONE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP421(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SAID_TO_BE_THE_SAME_AS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP460(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OPPOSITE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP461(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COLOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP462(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MEMBER_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP463(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SRGB_COLOR_HEX_TRIPLET {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP465(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ARCHIVES_AT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP485(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:UNICODE_CHARACTER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP487(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COUNTRY_OF_ORIGIN {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP495(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ENCLAVE_WITHIN {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP501(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CAUSE_OF_DEATH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP509(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ACADEMIC_DEGREE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP512(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INTERACTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP517(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPLIES_TO_PART {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP518(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TYPE_OF_ORBIT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP522(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_PART {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP527(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DIPLOMATIC_RELATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP530(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RESIDENCE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP551(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HANDEDNESS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP552(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:WEBSITE_ACCOUNT_ON {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP553(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TIME_OF_DISCOVERY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP575(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONFLICT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP607(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HIGHEST_POINT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP610(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATED_ON_TERRAIN_FEATURE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP706(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PARTICIPANT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP710(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FAMILY_NAME {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP734(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:GIVEN_NAME {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP735(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EDITION(S) {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP747(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPROVED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP790(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SIGNIFICANT_EVENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP793(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MASCOT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP822(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FEAST_DAY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP841(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TOPIC_MAIN_CATEGORY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP910(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NOTATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP913(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:WORK_LOCATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP937(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MEDICAL_CONDITION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1050(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:STUDENT_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1066(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TOPIC_MAIN_WIKIMEDIA_PORTAL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1151(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MANNER_OF_DEATH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1196(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PATENT_NUMBER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1246(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FACET_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1269(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEPICTED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1299(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OFFICE_HELD_BY_HEAD_OF_GOVERNMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1313(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PROVED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1318(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DESCRIBED_BY_SOURCE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1343(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:REPLACES {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1365(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CAPITAL_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1376(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LANGUAGES_SPOKEN_OR_WRITTEN {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1412(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SHAPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1419(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PET {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1429(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PRESENT_IN_WORK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1441(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LIST_OF_MONUMENTS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1456(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_FOR_PEOPLE_BORN_HERE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1464(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_FOR_PEOPLE_WHO_DIED_HERE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1465(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SOURCING_CIRCUMSTANCES {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1480(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CAUSE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1542(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MANIFESTATION_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1557(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LIFESTYLE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1576(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEEPEST_POINT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1589(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FICTIONAL_ANALOG_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1074(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_FOR_FILMS_SHOT_AT_THIS_LOCATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1740(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_OF_PEOPLE_BURIED_HERE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1791(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_OF_ASSOCIATED_PEOPLE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1792(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NAME_IN_KANA {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1814(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BLOOD_TYPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1853(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DIFFERENT_FROM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1889(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OFFICE_HELD_BY_HEAD_OF_STATE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1906(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ANTIPARTICLE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2152(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HISTORY_OF_TOPIC {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2184(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OFFICIAL_SYMBOL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2238(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);


  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:r {propId:{propId}, label:{propLabel}}]->(b)")
  void addRel(@Param("itemIdA") String itemIdA,
              @Param("itemIdB") String itemIdB,
              @Param("propId") String propId,
              @Param("propLabel") String propLabel);

}
