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

  /*
   * Invokes bulk data load to Neo4j from Wikidata dumps.  To speed up loading data, this endpoint takes a parameter
   * that indicates the subset (ending in a given digit) of the Wikidata items should be processed.
   *
   * @param onesdigit Subset (ending in a given digit) of the Wikidata items to be processed
   * @param startnum ID number (without the leading Q) of the item to begin processing
   * @param process Either "items" or "relationships".  Items must be processed successfully before processing relationships
   */
  @RequestMapping(value = "/wikidataload", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<Object> loadWikidata(@RequestParam(value = "onesdigit") String onesDigit,
                                             @RequestParam(value = "startnum") String startNum,
                                             @RequestParam(value = "process", defaultValue="items") String processType,
                                             @RequestParam(value = "lang", defaultValue="en") String lang) {

    String language = wikiBrowserProperties.computeLang(lang);
    String status = "OK";
    String userDir = System.getProperty("user.dir");

    log.info("WikidataLoader starting, onesDigit=" + onesDigit + ", startNum: " + startNum+ ", userDir: " + userDir);

    int onesDigitInt = 0;
    int startNumInt = 0;
    try {
      onesDigit = onesDigit.trim();
      onesDigitInt = Integer.parseInt(onesDigit);
      startNum = startNum.trim();
      startNumInt = Integer.parseInt(startNum);
      //if (userDir.equals("/Users/jamesweaver/wikidata-stuff/wikidata-loader")) {
      if (userDir.equals("/Users/jamesweaver/spring-guides/wikibrowser-service")) {
        log.info("********* Will begin processing, onesDigit=" + onesDigit + ", onesDigitInt =" + onesDigitInt + "**********");
        WikidataNeo4jProcessor wikidataNeo4jProcessor = new WikidataNeo4jProcessor(itemRepository, language, onesDigitInt, startNumInt, processType);
        ExampleHelpers
            .processEntitiesFromWikidataDump(wikidataNeo4jProcessor);
      }
      else {
        status = "Criteria for running not satisfied";
        log.info(status);
      }
    }
    catch (NumberFormatException nfe) {
      log.info("Invalid onesdigit, must be 0-9");
    }

    return Optional.ofNullable(status)  //TODO: Replace with indicator of how it went
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata load unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));

  }
}

