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
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataClaim;
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataItem;
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataProperty;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Bindings;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.ClaimsSparqlResponse;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Results;
import com.javafxpert.wikibrowser.model.locator.ItemInfoResponse;
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

import java.net.URI;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
public class WikiClaimsController {
  private static String WIKIDATA_ITEM_BASE = "http://www.wikidata.org/entity/";
  private static String WIKIDATA_PROP_BASE = "http://www.wikidata.org/prop/direct/";
  public static String WIKIPEDIA_BASE_TEMPLATE = "https://%s.wikipedia.org/";
  public static String WIKIPEDIA_MOBILE_BASE_TEMPLATE = "https://%s.m.wikipedia.org/";
  private static String WIKIPEDIA_TEMPLATE = "https://%s.wikipedia.org/wiki/";
  public static String WIKIPEDIA_MOBILE_TEMPLATE = "https://%s.m.wikipedia.org/wiki/";

  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiClaimsController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }

  @RequestMapping(value = "/claims", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> renderClaims(@RequestParam(value = "id", defaultValue="Q7259")
                                                                 String itemId,
                                                                 @RequestParam(value = "lang")
                                                                 String lang) {

    String language = wikiBrowserProperties.computeLang(lang);
    ClaimsSparqlResponse claimsSparqlResponse = callClaimsSparqlQuery(itemId, language);
    ClaimsResponse claimsResponse = convertSparqlResponse(claimsSparqlResponse, language, itemId);

    log.info("claimsResponse:" + claimsResponse);



    return Optional.ofNullable(claimsResponse)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));

  }


  @RequestMapping(value = "/claimsxml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
  public ResponseEntity<Object> renderClaimsXml(@RequestParam(value = "id", defaultValue="Q7259")
                                                             String itemId,
                                                             @RequestParam(value = "lang")
                                                             String lang) {

    String language = wikiBrowserProperties.computeLang(lang);
    ClaimsSparqlResponse claimsSparqlResponse = callClaimsSparqlQuery(itemId, language);
    ClaimsResponse claimsResponse = convertSparqlResponse(claimsSparqlResponse, language, itemId);

    return Optional.ofNullable(claimsResponse)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));

  }

  private ClaimsSparqlResponse callClaimsSparqlQuery(String itemId, String lang) {
    //TODO: Implement better way of creating the query represented by the following variables
    String wdqa = "https://query.wikidata.org/bigdata/namespace/wdq/sparql?format=json&query=";
    String wdqb = "PREFIX%20rdfs:%20%3Chttp://www.w3.org/2000/01/rdf-schema%23%3E%20";
    String wdqc = "PREFIX%20wikibase:%20%3Chttp://wikiba.se/ontology%23%3E%20";
    String wdqd = "PREFIX%20entity:%20%3Chttp://www.wikidata.org/entity/%3E%20";
    String wdqe = "PREFIX%20p:%20%3Chttp://www.wikidata.org/prop/direct/%3E%20";
    String wdqf = "SELECT%20?propUrl%20?propLabel%20?valUrl%20?valLabel%20";
    String wdqg = "WHERE%20%7B%20hint:Query%20hint:optimizer%20'None'%20.%20entity:";
    String wdqh = ""; // Some item ID e.g. Q7259
    String wdqi = "%20?propUrl%20?valUrl%20.%20?valUrl%20rdfs:label%20?valLabel%20FILTER%20(LANG(?valLabel)%20=%20'";
    String wdqj = ""; // Some language code e.g. en
    String wdqk = "')%20.%20?property%20?ref%20?propUrl%20.%20?property%20a%20wikibase:Property%20.%20";
    String wdql = "?property%20rdfs:label%20?propLabel%20FILTER%20(lang(?propLabel)%20=%20'";
    String wdqm = ""; // Some language code e.g. en
    String wdqn = "'%20)%20%7D%20ORDER%20BY%20?propUrl%20?valUrl%20LIMIT%20100";

    ClaimsSparqlResponse claimsSparqlResponse = null;

    wdqh = itemId;
    wdqj = lang;
    wdqm = lang;
    String wdQuery = wdqa + wdqb + wdqc + wdqd + wdqe + wdqf + wdqg + wdqh + wdqi + wdqj + wdqk + wdql + wdqm + wdqn;
    wdQuery = wdQuery.replaceAll(" ", "%20");
    log.info("wdQuery: " + wdQuery);

    try {

      claimsSparqlResponse = new RestTemplate().getForObject(new URI(wdQuery),
          ClaimsSparqlResponse.class);

      //log.info(claimsSparqlResponse.toString());

    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling wikidata sparql query " + e);
    }

    return claimsSparqlResponse;
  }

  private ClaimsResponse convertSparqlResponse(ClaimsSparqlResponse claimsSparqlResponse, String lang, String itemId) {
    ClaimsResponse claimsResponse = new ClaimsResponse();
    claimsResponse.setLang(lang);
    claimsResponse.setWdItem(itemId);
    claimsResponse.setWdItemBase(WIKIDATA_ITEM_BASE);
    claimsResponse.setWdPropBase(WIKIDATA_PROP_BASE);

    ItemInfoResponse itemInfoResponse = null;

    try {
      String url = this.wikiBrowserProperties.getLocatorServiceUrl(itemId, lang);
      itemInfoResponse = new RestTemplate().getForObject(url,
          ItemInfoResponse.class);

      log.info(itemInfoResponse.toString());
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling /locator?id=" + itemId + " : " + e);
    }

    if (itemInfoResponse != null) {
      claimsResponse.setArticleTitle(itemInfoResponse.getArticleTitle());
      claimsResponse.setArticleId(itemInfoResponse.getItemId());
    }

    //TODO: Consider implementing fallback to "en" if Wikipedia article doesn't exist in requested language
    claimsResponse.setWpBase(String.format(WIKIPEDIA_TEMPLATE, lang));

    //TODO: Consider implementing fallback to "en" if mobile Wikipedia article doesn't exist in requested language
    claimsResponse.setWpMobileBase(String.format(WIKIPEDIA_MOBILE_TEMPLATE, lang));

    Results results = claimsSparqlResponse.getResults();
    Iterator bindingsIter = results.getBindings().iterator();

    String lastPropId = "";
    WikidataClaim wikidataClaim = null; //TODO: Consider using exception handling to make null assignment unnecessary
    while (bindingsIter.hasNext()) {
      Bindings bindings = (Bindings)bindingsIter.next(); //TODO: Consider renaming Bindings to Binding

      // There is a 1:many relationship between property IDs and related values
      String nextPropUrl = bindings.getPropUrl().getValue();
      String nextPropId = nextPropUrl.substring(nextPropUrl.lastIndexOf("/") + 1);
      String nextValUrl = bindings.getValUrl().getValue();
      String nextValId = nextValUrl.substring(nextValUrl.lastIndexOf("/") + 1);
      //log.info("lastPropId: " + lastPropId + ", nextPropId: " + nextPropId);
      if (!nextPropId.equals(lastPropId)) {
        wikidataClaim = new WikidataClaim();
        wikidataClaim.setProp(new WikidataProperty(nextPropId, bindings.getPropLabel().getValue()));
        claimsResponse.getClaims().add(wikidataClaim);
        lastPropId = nextPropId;
      }
      wikidataClaim.addItem(new WikidataItem(nextValId, bindings.getValLabel().getValue()));
    }
    return claimsResponse;
  }

}

