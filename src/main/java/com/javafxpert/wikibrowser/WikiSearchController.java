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

import com.javafxpert.wikibrowser.model.locator.Item;
import com.javafxpert.wikibrowser.model.locator.ItemInfo;
import com.javafxpert.wikibrowser.model.locator.LocatorResponse;
import com.javafxpert.wikibrowser.model.locator.Sitelinks;
import com.javafxpert.wikibrowser.model.search.SearchFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseNear;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
@RequestMapping("/articlesearch")
public class WikiSearchController {
  private Log log = LogFactory.getLog(getClass());

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> search(@RequestParam(value = "title", defaultValue="")
                                                String title,
                                                @RequestParam(value = "lang", defaultValue="en")
                                                String lang) {

    //TODO: Implement better way of creating the query represented by the following variables

    String qa = "https://";
    String qb = ""; // Some language code e.g. en
    String qc = ".wikipedia.org/w/api.php?action=query&format=json&list=search&srlimit=10&srsearch=";
    String qd = ""; // article title so search for

    qb = lang;
    qd = title;

    String searchQuery = qa + qb + qc + qd;

    SearchResponseNear searchResponseNear = queryProcessSearchResponse(searchQuery, lang);

    return Optional.ofNullable(searchResponseNear)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikipedia title search unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Calls the Wikipedia search and returns an obect that holds results
   * @param searchQuery
   * @param lang
   * @return
   */
  private SearchResponseNear queryProcessSearchResponse(String searchQuery, String lang) {
    log.info("searchQuery: " + searchQuery);
    SearchResponseFar searchResponseFar = null;
    SearchResponseNear searchResponseNear = new SearchResponseNear();
    try {
      searchResponseFar = new RestTemplate().getForObject(searchQuery,
          SearchResponseFar.class);
      log.info(searchResponseFar.toString());

      String suggestion = searchResponseFar.getQueryFar().getSearchinfoFar().getSuggestion();
      if (suggestion != null && suggestion.length() > 0) {
        searchResponseNear.getTitles().add(suggestion);
      }

      Iterator iterator = searchResponseFar.getQueryFar().getSearchFar().iterator();
      while (iterator.hasNext()) {
        SearchFar searchFar = (SearchFar)iterator.next();
        searchResponseNear.getTitles().add(searchFar.getTitle());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling Wikipedia title search service " + e);
    }

    return searchResponseNear;
  }

}
