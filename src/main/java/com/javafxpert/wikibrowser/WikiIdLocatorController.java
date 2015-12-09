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

import com.javafxpert.wikibrowser.model.idlocator.IdLocatorResponse;
import com.javafxpert.wikibrowser.model.idlocator.IdQueryFar;
import com.javafxpert.wikibrowser.model.idlocator.PagePropsFar;
import com.javafxpert.wikibrowser.model.idlocator.PagesFar;
import com.javafxpert.wikibrowser.model.locator.Item;
import com.javafxpert.wikibrowser.model.locator.ItemInfo;
import com.javafxpert.wikibrowser.model.locator.LocatorResponse;
import com.javafxpert.wikibrowser.model.locator.Sitelinks;
import com.javafxpert.wikibrowser.model.search.QueryFar;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
@RequestMapping("/idlocator")
public class WikiIdLocatorController {
  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiIdLocatorController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> locatorEndpoint(@RequestParam(value = "name", defaultValue="")
                                                String articleName,
                                                @RequestParam(value = "lang")
                                                String lang) {

    String language = wikiBrowserProperties.computeLang(lang);

    ItemInfo itemInfo = null;
    if (!articleName.equals("")) {
      itemInfo = name2Id(articleName, language);
    }

    return Optional.ofNullable(itemInfo)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikipedia query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  public ItemInfo name2Id(String articleName, String lang) {

    //TODO: Implement better way of creating the query represented by the following variables
    String n2iqa = "https://";
    String n2iqb = ""; // Some language code e.g. en
    String n2iqc = ".wikipedia.org/w/api.php?action=query&prop=info%7Cpageprops&format=json&inprop=url&formatversion=2&redirects&titles=";
    String n2iqd = ""; // Some article name e.g. Ada_Lovelace

    n2iqb = lang;
    n2iqd = articleName;

    String wpQuery = n2iqa + n2iqb + n2iqc + n2iqd;

    return queryProcessIdLocatorResponse(wpQuery, lang);
  }

  private ItemInfo queryProcessIdLocatorResponse(String query, String lang) {
    query = query.replaceAll(" ", "%20");
    log.info("QUERY: " + query);
    IdLocatorResponse idLocatorResponse = null;
    ItemInfo itemInfo = new ItemInfo();
    try {
      idLocatorResponse = new RestTemplate().getForObject(new URI(query),
          IdLocatorResponse.class);

      log.info(idLocatorResponse.toString());

      IdQueryFar idQueryFar = idLocatorResponse.getIdQueryFar();
      List<PagesFar> pagesFarList = idQueryFar.getPagesFarList();
      if (pagesFarList.size() > 0) {
        PagesFar pageFar = pagesFarList.get(0);
        PagePropsFar pagePropsFar = pageFar.getPagePropsFar();

        String urlStr = pageFar.getFullUrl();
        String titleStr = pageFar.getTitle();
        String nameStr = urlStr.substring(urlStr.lastIndexOf("/") + 1);
        itemInfo.setArticleUrl(urlStr);
        itemInfo.setArticleTitle(titleStr);
        itemInfo.setArticleName(nameStr);
        itemInfo.setSite(lang + "wiki");
        itemInfo.setLang(lang);

        itemInfo.setItemId(pagePropsFar.getWikibaseItem());
      }
      else {
        log.info("no pages in results");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling IdLocator service " + e);
    }

    return itemInfo;
  }

}
