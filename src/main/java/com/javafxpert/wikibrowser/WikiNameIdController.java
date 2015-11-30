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
import com.javafxpert.wikibrowser.model.id2nameresponse.Id2NameResponse;
import com.javafxpert.wikibrowser.model.nameresponse.NameResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class WikiNameIdController {
  private Log log = LogFactory.getLog(getClass());

  //TODO: Implement better way of creating the query represented by the following variables
  private String i2nqa = "https://www.wikidata.org/w/api.php?action=wbgetentities&format=json&ids=";
  private String i2nqb = ""; // Some item ID e.g. Q7259
  private String i2nqc = "&props=sitelinks%7Csitelinks/urls&sitefilter=";
  private String i2nqd = ""; // Some language code e.g. en
  private String i2nqe = "wiki";

  @RequestMapping("/locator")
  public ResponseEntity<Object> callAndMarshallId2NameQuery(@RequestParam(value = "id", defaultValue="Q7259")
                                                                   String itemId,
                                                                 @RequestParam(value = "lang", defaultValue="en")
                                                                   String lang) {
    Id2NameResponse id2NameResponse = null;
    NameResponse nameResponse = null;

    i2nqb = itemId;
    i2nqd = lang;
    String wdQuery = i2nqa + i2nqb + i2nqc + i2nqd + i2nqe;
    log.info("wdQuery: " + wdQuery);

    try {
      id2NameResponse = new RestTemplate().getForObject(new URI(wdQuery),
          Id2NameResponse.class);

      log.info(id2NameResponse.toString());
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling wikidata ID to name service " + e);
    }

    /*
    claimsResponse = convertSparqlResponse(claimsSparqlResponse, lang, itemId);
    */

    nameResponse = new NameResponse("Article_Name", "enwiki", "http://foo.com", "Q0");

    return Optional.ofNullable(nameResponse)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /*
  private ClaimsResponse convertSparqlResponse(ClaimsSparqlResponse claimsSparqlResponse, String lang, String itemId) {
    ClaimsResponse claimsResponse = new ClaimsResponse();
    claimsResponse.setLang(lang);
    claimsResponse.setWdItem(itemId);
    claimsResponse.setWdItemBase(WIKIDATA_ITEM_BASE);
    claimsResponse.setWdPropBase(WIKIDATA_PROP_BASE);

    //TODO: Implement setting the language-specific article title
    claimsResponse.setArticleTitle("");

    //TODO: Implement setting the language-specific article ID
    claimsResponse.setArticleId("");

    //TODO: Implement fallback to "en" if Wikipedia article doesn't exist in requested language
    claimsResponse.setWpBase(String.format(WIKIPEDIA_TEMPLATE, lang));

    //TODO: Implement fallback to "en" if mobile Wikipedia article doesn't exist in requested language
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
      log.info("lastPropId: " + lastPropId + ", nextPropId: " + nextPropId);
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
  */
}

