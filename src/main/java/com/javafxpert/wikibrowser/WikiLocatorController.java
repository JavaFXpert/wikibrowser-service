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

import com.javafxpert.wikibrowser.model.locator.LocatorResponse;
import com.javafxpert.wikibrowser.model.locator.Item;
import com.javafxpert.wikibrowser.model.locator.Sitelinks;
import com.javafxpert.wikibrowser.model.locator.ItemInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
@RequestMapping("/locator")
public class WikiLocatorController {
  private Log log = LogFactory.getLog(getClass());

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> locatorEndpoint(@RequestParam(value = "id", defaultValue="")
                                                String itemId,
                                                @RequestParam(value = "lang", defaultValue="en")
                                                String lang) {

    ItemInfo itemInfo = null;
    if (!itemId.equals("")) {
      itemInfo = id2Name(itemId, lang);
    }

    return Optional.ofNullable(itemInfo)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikidata query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  public ItemInfo id2Name(String itemId, String lang) {
    LocatorResponse locatorResponse = null;
    ItemInfo itemInfo = null;

    //TODO: Implement better way of creating the query represented by the following variables
    String i2nqa = "https://www.wikidata.org/w/api.php?action=wbgetentities&format=json&ids=";
    String i2nqb = ""; // Some item ID e.g. Q7259
    String i2nqc = "&props=sitelinks%7Csitelinks/urls&sitefilter=";
    String i2nqd = ""; // Some language code e.g. en
    String i2nqe = "wiki";

    i2nqb = itemId;
    i2nqd = lang;
    String wdQuery = i2nqa + i2nqb + i2nqc + i2nqd + i2nqe;

    return queryProcessLocatorResponse(wdQuery, lang);
  }

  private ItemInfo queryProcessLocatorResponse(String query, String lang) {
    query = query.replaceAll(" ", "%20");
    log.info("query: " + query);
    LocatorResponse locatorResponse = null;
    ItemInfo itemInfo = new ItemInfo();

    try {
      locatorResponse = new RestTemplate().getForObject(new URI(query),
          LocatorResponse.class);

      log.info(locatorResponse.toString());

      Map<String, Item> itemMap = locatorResponse.getEntities();
      Item item = itemMap.get(itemMap.keySet().toArray()[0]);

      String itemId = item.getId();
      Item itemRef = itemMap.get(itemId);

      //TODO: Investigate why this is occasionally null and handle it better
      if (itemRef != null) {
        Map<String, Sitelinks> sitelinksMap = itemRef.getSitelinks();

        if (sitelinksMap != null && !sitelinksMap.isEmpty() && !sitelinksMap.keySet().isEmpty()) {
          Sitelinks sitelink = sitelinksMap.get(sitelinksMap.keySet().toArray()[0]);
          String urlStr = sitelink.getUrl();
          String titleStr = sitelink.getTitle();
          String nameStr = urlStr.substring(urlStr.lastIndexOf("/") + 1);
          itemInfo.setArticleUrl(urlStr);
          itemInfo.setArticleTitle(titleStr);
          itemInfo.setArticleName(nameStr);
          itemInfo.setSite(sitelink.getSite());
        }
      }
      else {
        log.info("item is null");
      }
      itemInfo.setLang(lang);
      itemInfo.setItemId(item.getId());
    }
    catch (Exception e) { //TODO: Consider moving catch down further in simlar method for all Controller classes
      e.printStackTrace();
      log.info("Caught exception when calling wikidata name to ID service " + e);
    }

    return itemInfo;
  }

}
