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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provides custom Cypher queries as repository search operations
 * Created by jamesweaver on 12/28/15.
 */
public interface ItemRepository extends GraphRepository<GraphItem> {

  // Temporary code to populate the properties-related code in this class
  static Map<Integer, String> propCodeMap = new TreeMap<>();

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
        //ReflectionUtils.invokeMethod(method, this, itemIdA, itemIdB, propId, propLabel);

        //TODO: Ascertain whether blank propLabel is OK, as it would avoid dups
        ReflectionUtils.invokeMethod(method, this, itemIdA, itemIdB, propId, propId.toLowerCase());
      }
      catch (Exception e) {
        System.out.println("Exception in invokeMethod " + methodStr + ": " + e);
        // TODO: Remove println above and decide how to report
      }
    }
    else {
      //addRel(itemIdA, itemIdB, propId, propLabel);

      //TODO: Ascertain whether blank propLabel is OK, as it would avoid dups
      addRel(itemIdA, itemIdB, propId, propId.toLowerCase());

      // Temporary code to populate the properties-related code in this class

      Integer propIdInt = new Integer(propId.substring(1));

      if (!propCodeMap.containsKey(propIdInt)) {
        String capsPropLabel = propLabel.trim().replaceAll(" ", "_").replaceAll("-", "_")
            .replaceAll("'", "").toUpperCase();

        String code = "  @Query(\"MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:" +
            capsPropLabel + " {propId:{propId}, label:{propLabel}}]->(b)\")\n";
        code += "  void " + methodStr +
            "(@Param(\"itemIdA\") String itemIdA, @Param(\"itemIdB\") String itemIdB, @Param(\"propId\") String propId, @Param(\"propLabel\") String propLabel);";

        propCodeMap.put(propIdInt, code);

        System.out.println("============Please paste the following methods in ItemRepository:===============");

        propCodeMap.forEach((k, v) -> {
          System.out.println(v + "\n");
        });

        System.out.println("\n\n============End of methods to paste in ItemRepository===========================");


        /*
        System.out.println("\n\nNeed to create method " + methodStr + "() in ItemRepository:\n\n");

        String capsPropLabel = propLabel.trim().replaceAll(" ", "_").toUpperCase();
        System.out.println("  @Query(\"MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:" +
            capsPropLabel + " {propId:{propId}, label:{propLabel}}]->(b)\")"
        );
        System.out.println("  void " + methodStr +
        "(@Param(\"itemIdA\") String itemIdA, @Param(\"itemIdB\") String itemIdB, @Param(\"propId\") String propId, @Param(\"propLabel\") String propLabel);"
        );

        System.out.println("\n\n");
        */
      }

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

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MOVEMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP135(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

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

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EDITIONS {propId:{propId}, label:{propLabel}}]->(b)")
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

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HIGHWAY_SYSTEM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP16(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MEMBER_OF_SPORTS_TEAM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP54(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DIRECTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP57(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SCREENWRITER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP58(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONSTELLATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP59(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONNECTING_LINE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP81(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ARCHITECT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP84(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COMPOSER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP86(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LIBRETTIST {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP87(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COMMISSIONED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP88(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SEXUAL_ORIENTATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP91(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MAIN_REGULATORY_TEXT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP92(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NOBLE_TITLE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP97(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EDITOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP98(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TAXON_RANK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP105(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ILLUSTRATOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP110(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MEASURED_PHYSICAL_QUANTITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP111(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FOUNDER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP112(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:AIRLINE_HUB {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP113(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:AIRLINE_ALLIANCE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP114(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HOME_VENUE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP115(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LEAGUE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP118(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ITEM_OPERATED {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP121(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MAINTAINED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP126(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_DIALECT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP134(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OPERATOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP137(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:IUCN_CONSERVATION_STATUS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP141(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BASED_ON {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP144(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ARCHITECTURAL_STYLE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP149(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:KILLED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP157(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HEADQUARTERS_LOCATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP159(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CAST_MEMBER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP161(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PRODUCER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP162(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CHIEF_EXECUTIVE_OFFICER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP169(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CREATOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP170(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PARENT_TAXON {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP171(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MANUFACTURER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP176(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CROSSES {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP177(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEPICTS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP180(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ENDEMIC_TO {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP183(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DOCTORAL_ADVISOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP184(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DOCTORAL_STUDENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP185(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MATERIAL_USED {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP186(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATION_OF_DISCOVERY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP189(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MAIN_BUILDING_CONTRACTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP193(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COLLECTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP195(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MINOR_PLANET_GROUP {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP196(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ADJACENT_STATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP197(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BUSINESS_DIVISION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP199(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LAKE_INFLOWS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP200(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LAKE_OUTFLOW {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP201(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BASIN_COUNTRY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP205(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PARTY_CHIEF_REPRESENTATIVE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP210(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OFFICIAL_RESIDENCE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP263(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PRODUCTION_COMPANY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP272(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP276(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HEAD_COACH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP286(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DESIGNER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP287(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:VESSEL_CLASS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP289(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DIRECTOR_OF_PHOTOGRAPHY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP344(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SUBSIDIARY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP355(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DISCOGRAPHY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP358(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PRESENTER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP371(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SPACE_LAUNCH_VEHICLE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP375(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATED_ON_ASTRONOMICAL_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP376(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PLATFORM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP400(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MOUTH_OF_THE_WATERCOURSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP403(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:GAME_MODE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP404(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SOUNDTRACK_ALBUM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP406(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LANGUAGE_OF_WORK_OR_NAME {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP407(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SOFTWARE_ENGINE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP408(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:VOICE_TYPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP412(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:POSITION_PLAYED_ON_TEAM_SPECIALITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP413(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:STOCK_EXCHANGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP414(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FIELD_OF_THIS_PROFESSION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP425(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TAXONOMIC_TYPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP427(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DISTRIBUTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP437(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATION_OF_SPACECRAFT_LAUNCH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP448(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ORIGINAL_NETWORK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP449(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ASTRONAUT_MISSION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP450(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PARTNER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP451(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INDUSTRY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP452(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FOUNDATIONAL_TEXT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP457(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OCCUPANT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP466(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LEGISLATED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP467(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LAKES_ON_RIVER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP469(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INPUT_DEVICE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP479(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RECORDED_AT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP483(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CHAIRPERSON {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP488(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CURRENCY_SYMBOL_DESCRIPTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP489(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EXCLAVE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP500(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:GENERAL_MANAGER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP505(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HONORIFIC_PREFIX {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP511(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:POWERPLANT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP516(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ARMAMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP520(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TEMPORAL_RANGE_START {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP523(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:STREAK_COLOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP534(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OFFICE_CONTESTED {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP541(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CRYSTAL_SYSTEM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP556(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TERMINUS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP559(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CENTRAL_BANK/ISSUER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP562(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BASIONYM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP566(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:IMA_STATUS_AND/OR_RANK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP579(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:POINT_GROUP {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP589(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RELIGIOUS_ORDER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP611(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MOTHER_HOUSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP612(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SOURCE_OF_ENERGY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP618(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:STRUCTURAL_ENGINEER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP631(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ROUTE_OF_ADMINISTRATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP636(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SPORT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP641(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TRANSLATOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP655(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ORGANIZER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP664(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATED_ON_STREET {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP669(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CHARACTERS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP674(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LYRICS_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP676(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:AFFLICTS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP689(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SPACE_GROUP {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP690(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CLEAVAGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP693(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DIOCESE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP708(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ASTEROID_SPECTRAL_TYPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP720(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:VOICE_ACTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP725(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INFLUENCED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP737(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:OBSOLETE_INFLUENCE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP738(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATION_OF_FORMATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP740(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PLAYING_HAND {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP741(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ASTEROID_FAMILY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP744(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPOINTED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP748(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PARENT_COMPANY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP749(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DISTRIBUTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP750(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ELECTORAL_DISTRICT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP768(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SIGNIFICANT_DRUG_INTERACTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP769(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CAUSE_OF_DESTRUCTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP770(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SYMPTOMS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP780(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NOTABLE_WORK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP800(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:STUDENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP802(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PROFESSORSHIP {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP803(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SEPARATED_FROM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP807(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:IUCN_PROTECTED_AREAS_CATEGORY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP814(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DECAYS_TO {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP816(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEDICATED_TO {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP825(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_CAUSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP828(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PUBLIC_HOLIDAY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP832(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DAY_IN_YEAR_FOR_PERIODIC_OCCURRENCE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP837(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NARRATIVE_LOCATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP840(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ESRB_RATING {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP852(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SPONSOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP859(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FOODS_TRADITIONALLY_ASSOCIATED {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP868(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PRINTED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP872(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PHASE_POINT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP873(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ORIGIN_OF_THE_WATERCOURSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP885(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PEGI_RATING {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP908(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:USK_RATING {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP914(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FILMING_LOCATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP915(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MAIN_SUBJECT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP921(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MAGNETIC_ORDERING {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP922(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MEDICAL_EXAMINATIONS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP923(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INSPIRED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP941(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:THEME_MUSIC {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP942(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PROGRAMMER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP943(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CODE_OF_NOMENCLATURE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP944(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ALLEGIANCE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP945(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATEGORY_COMBINES_TOPICS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP971(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TRIBUTARY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP974(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SUCCESSFUL_CANDIDATE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP991(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RECORD_HELD {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1000(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPLIES_TO_JURISDICTION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1001(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LANGUAGE_REGULATORY_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1018(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONFERRED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1027(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DONATED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1028(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MANAGER/DIRECTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1037(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RELATIVE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1038(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FILM_EDITOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1040(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEITY_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1049(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PRODUCT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1056(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PATHOGEN_TRANSMISSION_PROCESS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1060(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TRACK_GAUGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1064(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LOCATION_OF_FINAL_ASSEMBLY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1071(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:RECTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1075(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FROM_FICTIONAL_UNIVERSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1080(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:POLITICAL_IDEOLOGY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1142(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LAGRANGIAN_POINT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1145(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:APPROXIMATION_ALGORITHM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1171(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONNECTING_SERVICE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1192(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BODIES_OF_WATER_BASIN_CATEGORY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1200(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FILMOGRAPHY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1283(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:GODPARENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1290(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INSTRUMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1303(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CENTRAL_BANK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1304(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_FACET_POLYTOPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1312(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PLACE_OF_ORIGIN_SWITZERLAND {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1321(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DUAL_TO {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1322(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TERRITORY_CLAIMED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1336(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EYE_COLOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1340(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PARTICIPANT_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1344(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:WINNER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1346(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:REPLACED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1366(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:COINCIDENT_WITH {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1382(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONTAINS_SETTLEMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1383(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:POLITICAL_ALIGNMENT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1387(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONVICTED_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1399(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ORIGINAL_COMBINATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1403(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LICENSED_TO_BROADCAST_TO {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1408(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NOMINATED_FOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1411(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:GUI_TOOLKIT_OR_FRAMEWORK {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1414(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:AFFILIATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1416(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TAXON_SYNONYM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1420(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TOPICS_MAIN_TEMPLATE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1424(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:EXECUTIVE_PRODUCER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1431(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DESCRIBES_THE_FICTIONAL_UNIVERSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1434(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HERITAGE_STATUS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1435(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LEGAL_FORM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1454(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LIST_OF_WORKS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1455(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:STANDARDS_BODY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1462(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_IMMEDIATE_CAUSE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1478(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_CONTRIBUTING_FACTOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1479(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FAMILY_NAME_IDENTICAL_TO_THIS_FIRST_NAME {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1533(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MOTTO {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1546(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEPENDS_ON {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1547(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_QUALITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1552(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:GIVEN_NAME_VERSION_FOR_OTHER_GENDER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1560(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NATURAL_PRODUCT_OF_TAXON {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1582(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CONSECRATOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1598(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DRIVES_ON_THE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1622(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:UNDERCARRIAGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1637(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:WING_CONFIGURATION {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1654(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MPAA_FILM_RATING {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1657(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:THIS_TAXON_IS_SOURCE_OF {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1672(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:WIKIDATA_PROPERTY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1687(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:BRAND {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1716(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NAME_DAY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1750(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CHIEF_OPERATING_OFFICER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1789(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LIST_OF_EPISODES {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1811(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:REPRESENTED_BY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1875(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INCOME_CLASSIFICATION_PHILIPPINES {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1879(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LIST_OF_CHARACTERS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1881(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAIR_COLOR {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1884(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:CATHEDRAL {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1885(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:SECOND_SURNAME_IN_SPANISH_NAME {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1950(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:TAKEOFF_AND_LANDING_CAPABILITY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1956(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PATRON {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1962(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PROPERTIES_FOR_THIS_TYPE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1963(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:MEDICAL_SPECIALTY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1995(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:UNESCO_LANGUAGE_STATUS {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP1999(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ASPECT_RATIO {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2061(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:FABRICATION_METHOD {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2079(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:INTERNATIONAL_NUCLEAR_EVENT_SCALE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2127(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ARTERIAL_SUPPLY {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2286(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:LYMPHATIC_DRAINAGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2288(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:VENOUS_DRAINAGE {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2289(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:DEBUT_PARTICIPANT {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2318(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:PERIOD {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2348(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_LIST {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2354(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ROMAN_PRAENOMEN {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2358(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ROMAN_NOMEN_GENTILICIUM {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2359(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:NMHH_FILM_RATING {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2363(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ROMAN_COGNOMEN {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2365(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:ROMAN_AGNOMEN {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2366(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:HAS_SUPERPARTNER {propId:{propId}, label:{propLabel}}]->(b)")
  void addRelP2375(@Param("itemIdA") String itemIdA, @Param("itemIdB") String itemIdB, @Param("propId") String propId, @Param("propLabel") String propLabel);

  @Query("MATCH (a:Item {itemId:{itemIdA}}), (b:Item {itemId:{itemIdB}}) MERGE (a)-[:r {propId:{propId}, label:{propLabel}}]->(b)")
  void addRel(@Param("itemIdA") String itemIdA,
              @Param("itemIdB") String itemIdB,
              @Param("propId") String propId,
              @Param("propLabel") String propLabel);

}
