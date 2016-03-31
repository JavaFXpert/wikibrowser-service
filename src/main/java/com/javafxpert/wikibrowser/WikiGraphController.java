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
public class WikiGraphController {
  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiGraphController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }

  /**
   * Query Neo4j for all relationships between given set of item IDs
   * @param items
   * @return
   */
  @RequestMapping(value = "/graph", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> search(@RequestParam(value = "items", defaultValue="") String items) {
    // Example endpoint usage is graph?items=Q24, Q30, Q23, Q16, Q20
    // Scrub the input, and output a string for the Cypher query similar to the following:
    // 'Q24','Q30','Q23','Q16','Q20'
    String argStr = WikiBrowserUtils.scrubItemIds(items, true);

    log.info("argStr=" + argStr);

    GraphResponseNear graphResponseNear = null;

    if (argStr.length() == 0) {
      // TODO: Consider handling an invalid items argument better than the way it is handled here
      //argStr = "'Q2'"; // If the items argumentisn't valid, pretend Q2 (Earth) was entered
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
MATCH (a:Item)
WHERE a.itemId IN ['Q2', 'Q24', 'Q30']
OPTIONAL MATCH (a:Item)-[rel]-(b:Item)
WHERE b.itemId IN ['Q2', 'Q24', 'Q30']
RETURN a, b, collect(rel)
     */

      String qa = "{\"statements\":[{\"statement\":\"MATCH (a:Item) WHERE a.itemId IN [";
      String qb = argStr; // Item IDs
      String qc = "] OPTIONAL MATCH (a:Item)-[rel]-(b:Item) WHERE b.itemId IN [";
      String qd = argStr; // Item IDs
      String qe = "] RETURN a, b, collect(rel)\",";
      String qf = "\"resultDataContents\":[\"graph\"]}]}";

      String postString = qa + qb + qc + qd + qe + qf;

      graphResponseNear = queryProcessSearchResponse(neoCypherUrl, postString);
    }

    return Optional.ofNullable(graphResponseNear)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Graph query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Retrieve all paths through any properties, with a length of one or two hops, between two given item IDs.
   * TODO: Consider adding a LIMIT parameter
   * @param itemId
   * @param targetId
   * @return
   */
  @RequestMapping(value = "/shortpaths", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> retrieveShortPaths(@RequestParam(value = "id", defaultValue="Q1")
                                              String itemId,
                                              @RequestParam(value = "target", defaultValue="Q323")
                                              String targetId) {
    // Example endpoint usage is shortpaths?id=Q23&target=Q9696

    GraphResponseNear graphResponseNear = null;

    String neoCypherUrl = wikiBrowserProperties.getNeoCypherUrl();

    /*  Example Cypher query POST
    {
      "statements" : [ {
        "statement" : "MATCH p=allShortestPaths( (a:Item {itemId:'Q23'})-[*..2]-(b:Item {itemId:'Q9696'}) ) RETURN p",
        "resultDataContents" : ["graph" ]
      } ]
    }
    */

    /*
MATCH p=allShortestPaths( (a:Item {itemId:'Q23'})-[*..2]-(b:Item {itemId:'Q9696'}) )
RETURN p LIMIT 200
   */

    String qa = "{\"statements\":[{\"statement\":\"MATCH p=allShortestPaths( (a:Item {itemId:'";
    String qb = itemId; // starting item ID
    String qc = "'})-[*..2]-(b:Item {itemId:'";
    String qd = targetId; // target item ID
    String qe = "'}) ) WHERE NONE(x IN NODES(p) WHERE x:Item AND x.itemId = 'Q5') "; // Don't return paths that contain human item ID, or gender/described by relationships (slows query down)
    String qf = "AND NONE(y IN RELATIONSHIPS(p) WHERE y.propId = 'P21') AND NONE(y IN RELATIONSHIPS(p) WHERE y.propId = 'P1343') RETURN p LIMIT 200\",";
    String qg = "\"resultDataContents\":[\"graph\"]}]}";

    String postString = qa + qb + qc + qd + qe + qf + qg;

    graphResponseNear = queryProcessSearchResponse(neoCypherUrl, postString);

    return Optional.ofNullable(graphResponseNear)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("shortpaths query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Retrieve shortest path from a given item ID to the root of the items (Entity Q35120) that contains only
   * subclass-of (P279), instance-of (P31), and part-of
   * @param itemId
   * @return
   */
  @RequestMapping(value = "/rootpaths", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> retrieveShortestPathsToRoot(@RequestParam(value = "id", defaultValue="Q2") String itemId) {
    // Example endpoint usage is rootpath?id=Q319

    String targetId = "Q35120"; // Wikidata Entity item ID

    GraphResponseNear graphResponseNear = null;

    String neoCypherUrl = wikiBrowserProperties.getNeoCypherUrl();

    /*  Example Cypher query POST
    {
      "statements" : [ {
        "statement" : "MATCH p=allShortestPaths( (a:Item {itemId:'Q319'})-[*]->(b:Item {itemId:'Q35120'}) ) WHERE NONE(x IN RELATIONSHIPS(p) WHERE (x.propId <> 'P279') AND (x.propId <> 'P31') AND (x.propId <> 'P361')) RETURN p",
        "resultDataContents" : ["graph" ]
      } ]
    }
    */

    /*
MATCH p=shortestPath( (a:Item {itemId:'Q319'})-[*]->(b:Item {itemId:'Q35120'}) )
WHERE NONE(x IN RELATIONSHIPS(p) WHERE (x.propId <> 'P279') AND (x.propId <> 'P31') AND (x.propId <> 'P361'))
RETURN p
   */

    String qa = "{\"statements\":[{\"statement\":\"MATCH p=allShortestPaths( (a:Item {itemId:'";
    String qb = itemId; // starting item ID
    String qc = "'})-[*]->(b:Item {itemId:'";
    String qd = targetId; // target item ID
    // Regard a path if it contains only subclass-of, instance-of, and part-of relationships
    String qe = "'}) ) WHERE NONE(x IN RELATIONSHIPS(p) WHERE (x.propId <> 'P279') AND (x.propId <> 'P31') AND (x.propId <> 'P361')) ";
    String qf = "RETURN p\",";
    String qg = "\"resultDataContents\":[\"graph\"]}]}";

    String postString = qa + qb + qc + qd + qe + qf + qg;

    graphResponseNear = queryProcessSearchResponse(neoCypherUrl, postString);

    return Optional.ofNullable(graphResponseNear)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("rootpaths query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
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
      log.info("graphResponseFar: " + graphResponseFar);

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
