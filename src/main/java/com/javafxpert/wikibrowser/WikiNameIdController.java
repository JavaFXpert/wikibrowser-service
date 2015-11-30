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
import com.javafxpert.wikibrowser.model.id2nameresponse.Item;
import com.javafxpert.wikibrowser.model.id2nameresponse.Sitelinks;
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
import java.util.Map;
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

    nameResponse = convertId2NameResponse(id2NameResponse, lang, itemId);

    //nameResponse = new NameResponse("Article_Name", "enwiki", "http://foo.com", "Q0");

    return Optional.ofNullable(nameResponse)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  private NameResponse convertId2NameResponse(Id2NameResponse id2NameResponse, String lang, String itemId) {
    NameResponse nameResponse = new NameResponse();
    Map<String, Item> itemMap = id2NameResponse.getEntities();
    Map<String, Sitelinks> sitelinksMap = itemMap.get(itemId).getSitelinks();
    Sitelinks sitelink = sitelinksMap.get(sitelinksMap.keySet().toArray()[0]);
    String urlStr = sitelink.getUrl();
    String nameStr = urlStr.substring(urlStr.lastIndexOf("/") + 1);

    nameResponse.setArticleUrl(urlStr);
    nameResponse.setArticleName(nameStr);
    nameResponse.setSite(sitelink.getSite());
    nameResponse.setLang(lang);
    nameResponse.setItemId(itemId);
    return nameResponse;
  }
}

