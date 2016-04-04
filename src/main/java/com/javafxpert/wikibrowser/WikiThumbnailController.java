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

import com.javafxpert.wikibrowser.model.locator.ItemInfoResponse;
import com.javafxpert.wikibrowser.model.thumbnail.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
@RequestMapping("/thumbnail")
public class WikiThumbnailController {
  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiThumbnailController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> locatorEndpoint(@RequestParam(value = "title", defaultValue="")
                                                String articleTitle,
                                                @RequestParam(value = "id", defaultValue="")
                                                String itemId,
                                                @RequestParam(value = "lang")
                                                String lang) {

    String language = wikiBrowserProperties.computeLang(lang);

    String thumbnailUrlStr = null;
    if (!articleTitle.equals("")) {
      // Check cache for thumbnail
      thumbnailUrlStr = ThumbnailCache.getThumbnailUrl(articleTitle, language);

      if (thumbnailUrlStr == null || thumbnailUrlStr.length() == 0) {
        log.info("Thumbnail NOT found in cache for articleTitle: " + articleTitle + ", lang: " + lang);

        thumbnailUrlStr = title2Thumbnail(articleTitle, language);
        ThumbnailCache.setThumbnailUrl(articleTitle, language, thumbnailUrlStr);
      }
    }
    else if (!itemId.equals("")) {
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
        thumbnailUrlStr = title2Thumbnail(itemInfoResponse.getArticleTitle(), language);
      }
    }

    return Optional.ofNullable(thumbnailUrlStr)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikipedia thumbnail query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  public String title2Thumbnail(String articleTitle, String lang) {

    //TODO: Implement better way of creating the query represented by the following variables
    String n2iqa = "https://";
    String n2iqb = ""; // Some language code e.g. en
    String n2iqc = ".wikipedia.org/w/api.php?action=query&format=json&prop=pageimages&formatversion=2&piprop=thumbnail&pithumbsize=200&titles=";
    String n2iqd = ""; // Some article name e.g. Ada Lovelace

    n2iqb = lang;
    n2iqd = articleTitle;

    String wpQuery = n2iqa + n2iqb + n2iqc + n2iqd;

    return queryProcessThumbnailResponse(wpQuery, lang);
  }

  private String queryProcessThumbnailResponse(String query, String lang) {
    query = query.replaceAll(" ", "%20");
    log.info("QUERY: " + query);
    ThumbnailResponse thumbnailResponse = null;
    String thumbnailUrlStr = "";
    try {
      thumbnailResponse = new RestTemplate().getForObject(new URI(query),
          ThumbnailResponse.class);

      log.info(thumbnailResponse.toString());

      ThumbnailQueryFar thumbnailQueryFar = thumbnailResponse.getThumbnailQueryFar();
      List<ThumbnailPagesFar> thumbnailPagesFarList = thumbnailQueryFar.getThumbnailPagesFarList();
      if (thumbnailPagesFarList.size() > 0) {
        ThumbnailPagesFar thumbnailPageFar = thumbnailPagesFarList.get(0);
        ThumbnailFar thumbnailFar = thumbnailPageFar.getThumbnailFar();

        if (thumbnailFar != null) {
          thumbnailUrlStr = thumbnailFar.getSource();
        }
      }
      else {
        log.info("no pages in results");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling Thumbnail service " + e);
    }

    return thumbnailUrlStr;
  }

}
