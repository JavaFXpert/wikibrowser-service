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

import com.javafxpert.wikibrowser.model.conceptmap.DummyResponseFar;
import com.javafxpert.wikibrowser.model.conceptmap.GraphResponseFar;
import com.javafxpert.wikibrowser.model.conceptmap.GraphResponseNear;
import com.javafxpert.wikibrowser.model.conceptmap.ItemServiceImpl;
import com.javafxpert.wikibrowser.model.search.SearchFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseNear;
import com.javafxpert.wikibrowser.model.search.SearchinfoFar;
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
import java.util.Arrays;
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
  public ResponseEntity<Object> search(@RequestParam(value = "items", defaultValue="") String items) {

    String neoCypherUrl = wikiBrowserProperties.getNeoCypherUrl();

    /*
    {
      "statements" : [ {
        "statement" : "MATCH (a)-[r]->(b) WHERE a.itemId IN ['Q24', 'Q30', 'Q23', 'Q16', 'Q20'] AND b.itemId IN ['Q24', 'Q30', 'Q23', 'Q16', 'Q20'] RETURN a, b, r",
        "resultDataContents" : ["graph" ]
      } ]
    }
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

    String username = "wikibrowserdb"; //TODO: Make a configuration setting
    String password = "sKZ88sPptVL6INjQEeQk"; //TODO: Make a configuration setting

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = this.createHeaders(username, password);
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity request = new HttpEntity(postString, httpHeaders);

    GraphResponseFar graphResponseFar = null;
    GraphResponseNear graphResponseNear = new GraphResponseNear();
    try {
      ResponseEntity<GraphResponseFar> result = restTemplate.exchange(neoCypherUrl, HttpMethod.POST, request,
          GraphResponseFar.class);
      graphResponseFar = result.getBody();
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

  /**
   * TODO: Move this to a util class
   * @param username
   * @param password
   * @return
   */
  private HttpHeaders createHeaders(final String username, final String password ){
    HttpHeaders headers =  new HttpHeaders(){
      {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
            auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        set( "Authorization", authHeader );
      }
    };
    headers.add("Content-Type", "application/xml");
    headers.add("Accept", "application/xml");

    return headers;
  }

}
