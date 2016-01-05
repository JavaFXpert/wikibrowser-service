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

package com.javafxpert.wikibrowser;

import com.javafxpert.wikibrowser.model.conceptmap.*;
import com.javafxpert.wikibrowser.model.search.SearchFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseNear;
import com.javafxpert.wikibrowser.model.search.SearchinfoFar;
import com.javafxpert.wikibrowser.util.WikiBrowserUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
@RequestMapping("/graph")
public class WikiGraphController {
  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiGraphController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }


  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> search(@RequestParam(value = "items", defaultValue="") String items) {
    // Example endpoint usage is graph?items=Q24, Q30, Q23, Q16, Q20
    // Scrub the input, and output a string for the Cypher query similar to the following:
    // 'Q24','Q30','Q23','Q16','Q20'
    String[] itemsArray = items.split(",");
    String argStr = "";
    for (int i = 0; i < itemsArray.length; i++) {
      String itemStr = itemsArray[i];
      itemStr = itemStr.trim().toUpperCase();
      if (itemStr.length() > 0 && itemStr.substring(0, 1).equals("Q")) {
        argStr += "'" + itemStr + "'";
        if (i < itemsArray.length - 1) {
          argStr += ",";
        }
      }
    }
    log.info("argStr=" + argStr);

    GraphResponseNear graphResponseNear = null;

    if (argStr.length() == 0) {
      // TODO: Consider handling an invalid items argument better than the way it is handled here
      argStr = "'Q2'"; // If the items argumentisn't valid, pretend Q2 (Earth) was entered
    }

    if (argStr.length() > 0) {
      String neoCypherUrl = wikiBrowserProperties.getNeoCypherUrl();

      /*  Example Cypher query POST
      {
        "statements" : [ {
          "statement" : "MATCH (a)-[r]->(b) WHERE a.itemId IN ['Q24', 'Q30', 'Q23', 'Q16', 'Q20'] AND b.itemId IN ['Q24', 'Q30', 'Q23', 'Q16', 'Q20'] RETURN a, b, r",
          "resultDataContents" : ["graph" ]
        } ]
      }
      */

      /*
MATCH (a)
WHERE a.itemId IN ['Q2', 'Q24', 'Q30', 'Q23', 'Q16', 'Q20']
OPTIONAL MATCH (a)-[rel]-()
RETURN a, collect(rel)

     */

      String qa = "{\"statements\":[{\"statement\":\"MATCH (a) WHERE a.itemId IN [";
      String qb = argStr; // Item IDs
      String qc = "] OPTIONAL MATCH (a)-[rel]-() RETURN a, collect(rel)\",";
      String qd = "\"resultDataContents\":[\"graph\"]}]}";

      String postString = qa + qb + qc + qd;

      graphResponseNear = queryProcessSearchResponse(neoCypherUrl, postString);
    }

    return Optional.ofNullable(graphResponseNear)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Graph query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Calls the Neo4j Transactional Cypher service and returns an object that holds results
   * @param neoCypherUrl
   * @param postString
   * @return
   */
  private GraphResponseNear queryProcessSearchResponse(String neoCypherUrl, String postString) {
    log.info("neoCypherUrl: " + neoCypherUrl);
    log.info("postString: " + postString);

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders httpHeaders = WikiBrowserUtils.createHeaders(wikiBrowserProperties.getCypherUsername(),
        wikiBrowserProperties.getCypherPassword());

    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity request = new HttpEntity(postString, httpHeaders);

    GraphResponseFar graphResponseFar = null;
    GraphResponseNear graphResponseNear = new GraphResponseNear();
    try {
      ResponseEntity<GraphResponseFar> result = restTemplate.exchange(neoCypherUrl, HttpMethod.POST, request,
          GraphResponseFar.class);
      graphResponseFar = result.getBody();

      // Populate GraphResponseNear instance from GraphResponseFar instance
      HashMap<String, GraphNodeNear> graphNodeNearMap = new HashMap<>();
      HashMap<String, GraphLinkNear> graphLinkNearMap = new HashMap<>();

      List<ResultFar> resultFarList = graphResponseFar.getResultFarList();
      if (resultFarList.size() > 0) {
        List<DataFar> dataFarList = resultFarList.get(0).getDataFarList();
        Iterator<DataFar> dataFarIterator = dataFarList.iterator();

        while (dataFarIterator.hasNext()) {
          GraphFar graphFar = dataFarIterator.next().getGraphFar();

          List<GraphNodeFar> graphNodeFarList = graphFar.getGraphNodeFarList();
          Iterator<GraphNodeFar> graphNodeFarIterator = graphNodeFarList.iterator();

          while (graphNodeFarIterator.hasNext()) {
            GraphNodeFar graphNodeFar = graphNodeFarIterator.next();
            GraphNodeNear graphNodeNear = new GraphNodeNear();

            graphNodeNear.setId(graphNodeFar.getGraphNodePropsFar().getItemId());
            graphNodeNear.setTitle(graphNodeFar.getGraphNodePropsFar().getTitle());
            graphNodeNear.setLabelsList(graphNodeFar.getLabelsList());

            // Note: The key in the graphNodeNearMap is the Neo4j node id, not the Wikidata item ID
            graphNodeNearMap.put(graphNodeFar.getId(), graphNodeNear);
          }

          List<GraphRelationFar> graphRelationFarList = graphFar.getGraphRelationFarList();
          Iterator<GraphRelationFar> graphRelationFarIterator = graphRelationFarList.iterator();

          while (graphRelationFarIterator.hasNext()) {
            GraphRelationFar graphRelationFar = graphRelationFarIterator.next();
            GraphLinkNear graphLinkNear = new GraphLinkNear();

            // Use the Neo4j node ids from the relationship to retrieve the Wikidata Item IDs from the graphNodeNearMap
            String neo4jStartNodeId = graphRelationFar.getStartNode();
            String wikidataStartNodeItemId = graphNodeNearMap.get(neo4jStartNodeId).getId();
            String neo4jEndNodeId = graphRelationFar.getEndNode();
            String wikidataEndNodeItemId = graphNodeNearMap.get(neo4jEndNodeId).getId();

            graphLinkNear.setSource(wikidataStartNodeItemId);
            graphLinkNear.setTarget(wikidataEndNodeItemId);
            graphLinkNear.setPropId(graphRelationFar.getGraphRelationPropsFar().getPropId());
            graphLinkNear.setLabel(graphRelationFar.getGraphRelationPropsFar().getLabel());
            graphLinkNear.setType(graphRelationFar.getType());

            // Note: The key in the graphLinkNearMap is the Neo4j node id, not the Wikidata item ID
            graphLinkNearMap.put(graphRelationFar.getId(), graphLinkNear);
          }
        }

        // Create and populate a List of nodes to set into the graphResponseNear instance
        List<GraphNodeNear> graphNodeNearList = new ArrayList<>();
        graphNodeNearMap.forEach((k, v) -> {
          graphNodeNearList.add(v);
        });
        graphResponseNear.setGraphNodeNearList(graphNodeNearList);

        // Create and populate a List of links to set into the graphResponseNear instance
        List<GraphLinkNear> graphLinkNearList = new ArrayList<>();
        graphLinkNearMap.forEach((k, v) -> {
          graphLinkNearList.add(v);
        });
        graphResponseNear.setGraphLinkNearList(graphLinkNearList);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling Neo Cypher service " + e);
    }

    return graphResponseNear;
  }
}
