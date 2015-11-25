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

import com.javafxpert.wikibrowser.model.claimsresponse.ClaimsResponse;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.ClaimsSparqlResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
public class WikiBrowserController {
  private Log log = LogFactory.getLog(getClass());

  //TODO: Implement better way of creating the query represented by the following variables
  private String wdqa = "https://query.wikidata.org/bigdata/namespace/wdq/sparql?format=json&query=";
  private String wdqb = "PREFIX%20rdfs:%20%3Chttp://www.w3.org/2000/01/rdf-schema%23%3E%20";
  private String wdqc = "PREFIX%20wikibase:%20%3Chttp://wikiba.se/ontology%23%3E%20";
  private String wdqd = "PREFIX%20entity:%20%3Chttp://www.wikidata.org/entity/%3E%20";
  private String wdqe = "PREFIX%20p:%20%3Chttp://www.wikidata.org/prop/direct/%3E%20";
  private String wdqf = "SELECT%20?propUrl%20?propLabel%20?valUrl%20?valLabel%20";
  private String wdqg = "WHERE%20%7B%20hint:Query%20hint:optimizer%20'None'%20.%20entity:";
  private String wdqh = ""; // Some item ID e.g. Q7259
  private String wdqi = "%20?propUrl%20?valUrl%20.%20?valUrl%20rdfs:label%20?valLabel%20FILTER%20(LANG(?valLabel)%20=%20'";
  private String wdqj = ""; // Some language code e.g. en
  private String wdqk = "')%20.%20?property%20?ref%20?propUrl%20.%20?property%20a%20wikibase:Property%20.%20";
  private String wdql = "?property%20rdfs:label%20?propLabel%20FILTER%20(lang(?propLabel)%20=%20'";
  private String wdqm = ""; // Some language code e.g. en
  private String wdqn = "')%20%7D%20LIMIT%20100";

  @RequestMapping("/claims")
  public ResponseEntity<Object> callAndMarshallClaimsSparqlQuery(@RequestParam(value = "id", defaultValue="Q7259")
                                                                   String itemId,
                                                                 @RequestParam(value = "lang", defaultValue="en")
                                                                   String language) {
    ClaimsSparqlResponse claimsSparqlResponse = null;
    ClaimsResponse claimsResponse = null;

    wdqh = itemId;
    wdqj = language;
    wdqm = language;
    String wdQuery = wdqa + wdqb + wdqc + wdqd + wdqe + wdqf + wdqg + wdqh + wdqi + wdqj + wdqk + wdql + wdqm + wdqn;
    log.info("wdQuery: " + wdQuery);

    try {

      claimsSparqlResponse = new RestTemplate().getForObject(new URI(wdQuery),
          ClaimsSparqlResponse.class);

      log.info(claimsSparqlResponse.toString());

    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling wikidata sparql query " + e);
    }

    claimsResponse = convertSparqlResponse(claimsSparqlResponse);

    return Optional.ofNullable(claimsResponse)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));

  }

  private ClaimsResponse convertSparqlResponse(ClaimsSparqlResponse claimsSparqlResponse) {
    ClaimsResponse claimsResponse = null;

    return claimsResponse;
  }

}

