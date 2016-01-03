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

import com.javafxpert.wikibrowser.model.conceptmap.GraphResponseFar;
import com.javafxpert.wikibrowser.model.conceptmap.GraphResponseNear;
import com.javafxpert.wikibrowser.model.conceptmap.ItemServiceImpl;
import com.javafxpert.wikibrowser.model.search.SearchFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseNear;
import com.javafxpert.wikibrowser.model.search.SearchinfoFar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Optional;

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
  public ResponseEntity<Object> search(@RequestParam(value = "items", defaultValue="") String title) {

    String neoCypherUrl = wikiBrowserProperties.getNeoCypherUrl();

    /*
    {
      "statements" : [ {
        "statement" : "MATCH (a)-[r]->(b) WHERE a.itemId IN ['Q24', 'Q30', 'Q23', 'Q16', 'Q20'] AND b.itemId IN ['Q24', 'Q30', 'Q23', 'Q16', 'Q20'] RETURN a, b, r",
        "resultDataContents" : ["graph" ]
      } ]
    }
    */

    /*
    String qa = "%7B\"statements\":[%7B\"statement\":\"MATCH%20(a)-[r]-%3E(b)%20WHERE%20a.itemId%20IN%20[";
    String qb = "'Q24','Q30','Q23','Q16','Q20'"; // Item IDs
    String qc = "]%20AND%20b.itemId%20IN%20[";
    String qd = "'Q24','Q30','Q23','Q16','Q20'"; // Item IDs
    String qe = "%20RETURN%20a,b,r\",\"resultDataContents\":[\"graph\"]%7D]%7D";
    */

    String qa = "{\"statements\":[{\"statement\":\"MATCH (a)-[r]->(b) WHERE a.itemId IN [";
    String qb = "'Q24','Q30','Q23','Q16','Q20'"; // Item IDs
    String qc = "] AND b.itemId IN [";
    String qd = "'Q24','Q30','Q23','Q16','Q20'"; // Item IDs
    String qe = "] RETURN a, b, r\",\"resultDataContents\":[\"graph\"]}]}";

    String postString = qa + qb + qc + qd + qe;

    GraphResponseNear graphResponseNear = queryProcessSearchResponse(neoCypherUrl, postString);

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
    GraphResponseFar graphResponseFar = null;
    GraphResponseNear graphResponseNear = new GraphResponseNear();
    try {
      graphResponseFar = new RestTemplate().postForObject(neoCypherUrl, postString,
          GraphResponseFar.class);
      log.info(graphResponseFar.toString());

      /*
      Iterator iterator = searchResponseFar.getQueryFar().getSearchFar().iterator();
      while (iterator.hasNext()) {
        SearchFar searchFar = (SearchFar)iterator.next();
        searchResponseNear.getTitles().add(searchFar.getTitle());
      }
      */
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling Neo Cypher service " + e);
    }

    return graphResponseNear;
  }

}
