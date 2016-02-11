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

package com.javafxpert.wikibrowser.util;

import com.javafxpert.wikibrowser.WikiBrowserProperties;
import com.javafxpert.wikibrowser.model.claimsresponse.ClaimsResponse;
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataClaim;
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataItem;
import com.javafxpert.wikibrowser.model.claimsresponse.WikidataProperty;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Bindings;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.ClaimsSparqlResponse;
import com.javafxpert.wikibrowser.model.claimssparqlresponse.Results;
import com.javafxpert.wikibrowser.model.conceptmap.ItemRepository;
import com.javafxpert.wikibrowser.model.conceptmap.ItemServiceImpl;
import com.javafxpert.wikibrowser.model.locator.ItemInfoResponse;
import com.javafxpert.wikibrowser.model.traversalresponse.TraversalBindingsFar;
import com.javafxpert.wikibrowser.model.traversalresponse.TraversalResponse;
import com.javafxpert.wikibrowser.model.traversalresponse.TraversalResultsFar;
import com.javafxpert.wikibrowser.model.traversalresponse.TraversalSparqlResponse;
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
public class WikidataLoader {
  //private static String WIKIDATA_ITEM_BASE = "http://www.wikidata.org/entity/";

  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  private final ItemServiceImpl itemService;

  private ItemRepository itemRepository;

  @Autowired
  public WikidataLoader(WikiBrowserProperties wikiBrowserProperties, ItemServiceImpl itemService) {
    this.wikiBrowserProperties = wikiBrowserProperties;
    this.itemService = itemService;
    itemRepository = itemService.getItemRepository();
  }

  @RequestMapping(value = "/wikidataload", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<Object> loadWikidata(@RequestParam(value = "confirm") String confirm,
                                             @RequestParam(value = "lang", defaultValue="en") String lang) {

    String language = wikiBrowserProperties.computeLang(lang);
    String status = "OK";
    String userDir = System.getProperty("user.dir");

    log.info("WikidataLoader starting, confirm=" + confirm + ", userDir: " + userDir);

    if (confirm.equals("yes") && userDir.equals("/Users/jamesweaver/spring-guides/wikibrowser-service")) {
      log.info("Will begin processing");
    }
    else {
      status = "Criteria for running not satisfied";
      log.info(status);
    }

    WikidataNeo4jProcessor wikidataNeo4jProcessor = new WikidataNeo4jProcessor(itemRepository, language);
    ExampleHelpers
        .processEntitiesFromWikidataDump(wikidataNeo4jProcessor);

    /*
    if (itemInfoResponse != null) {
      claimsResponse.setArticleTitle(itemInfoResponse.getArticleTitle());
      claimsResponse.setArticleId(itemInfoResponse.getItemId());

      // MERGE item into Neo4j graph
      if (claimsResponse.getArticleId() != null && claimsResponse.getArticleTitle() != null) {
        //log.info("====== itemRepository.addItem: " + claimsResponse.getArticleId() + ", " + claimsResponse.getArticleTitle());
        itemRepository.addItem(claimsResponse.getArticleId(), claimsResponse.getArticleTitle());
      }
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

      WikidataItem wikidataItem = new WikidataItem(nextValId, bindings.getValLabel().getValue());
      wikidataClaim.addItem(wikidataItem);

      // MERGE item and relationships into Neo4j graph
      if (itemId != null &&
          wikidataItem.getId() != null &&
          wikidataClaim.getProp().getId() != null &&
          wikidataClaim.getProp().getLabel() != null) {

        // Write item
        //log.info("++++++ itemRepository.addItem: " + wikidataItem.getId() + ", " + wikidataItem.getLabel());
        itemRepository.addItem(wikidataItem.getId(), wikidataItem.getLabel());

        // Write relationship
        //log.info("------ itemRepository.addRelationship: " + itemId + ", " +
        //                 wikidataItem.getId() + ", " +
        //                 wikidataClaim.getProp().getId() + ", " +
        //                 wikidataClaim.getProp().getLabel());

        itemRepository.addRelationship(itemId,
            wikidataItem.getId(),
            wikidataClaim.getProp().getId(),
            wikidataClaim.getProp().getLabel());
      }
    }
    */


    return Optional.ofNullable(status)  //TODO: Replace with indicator of how it went
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata load unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));

  }
}

