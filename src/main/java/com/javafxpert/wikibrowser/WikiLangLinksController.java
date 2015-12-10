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

import com.javafxpert.wikibrowser.model.idlocator.*;
import com.javafxpert.wikibrowser.model.language.LangLinkInfo;
import com.javafxpert.wikibrowser.model.language.LangLinksResponseNear;
import com.javafxpert.wikibrowser.model.locator.ItemInfo;
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
import java.util.List;
import java.util.Optional;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
@RequestMapping("/langlinks")
public class WikiLangLinksController {
  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiLangLinksController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> locatorEndpoint(@RequestParam(value = "title")
                                                String articleTitle,
                                                @RequestParam(value = "lang")
                                                String lang) {

    String language = wikiBrowserProperties.computeLang(lang);

    LangLinksResponseNear langLinksResponseNear = null;
    if (!articleTitle.equals("")) {
      langLinksResponseNear = retrieveLangLinks(articleTitle, language);
    }

    return Optional.ofNullable(langLinksResponseNear)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Wikipedia query unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  public LangLinksResponseNear retrieveLangLinks(String articleTitle, String lang) {

    //TODO: Implement better way of creating the query represented by the following variables
    String n2iqa = "https://";
    String n2iqb = ""; // Some language code e.g. en
    String n2iqc = ".wikipedia.org/w/api.php?action=query&formatversion=2&prop=langlinks&format=json&llprop=url%7Clangname%7Cautonym&lldir=ascending&redirects&lllimit=300&titles=";
    String n2iqd = ""; // Some article title e.g. "Ada Lovelace"

    n2iqb = lang;
    n2iqd = articleTitle;

    String wpQuery = n2iqa + n2iqb + n2iqc + n2iqd;

    return queryProcessLangLinksResponse(wpQuery, lang);
  }

  private LangLinksResponseNear queryProcessLangLinksResponse(String query, String lang) {
    query = query.replaceAll(" ", "%20");
    log.info("langlinks query: " + query);
    IdLocatorResponse wpQueryReponse = null; //TODO: Rename IdLocatorResponse class to WpQueryReponse
    LangLinksResponseNear langLinksResponseNear = new LangLinksResponseNear();
    try {
      wpQueryReponse = new RestTemplate().getForObject(new URI(query),
          IdLocatorResponse.class);

      log.info(wpQueryReponse.toString());

      IdQueryFar idQueryFar = wpQueryReponse.getIdQueryFar();
      List<PagesFar> pagesFarList = idQueryFar.getPagesFarList();
      if (pagesFarList.size() > 0) {
        PagesFar pageFar = pagesFarList.get(0);
        List <LangLinkFar> langLinkFarList = pageFar.getLangLinkFarList();

        Iterator<LangLinkFar> langLinksIter = langLinkFarList.iterator();
        List<LangLinkInfo> langLinkInfoList = langLinksResponseNear.getLangLinkInfoList();
        while (langLinksIter.hasNext()) {
          LangLinkFar langLinkFar = langLinksIter.next();
          LangLinkInfo langLinkInfo = new LangLinkInfo();

          String articleUrlStr = langLinkFar.getArticleUrl();
          String articleName = articleUrlStr.substring(articleUrlStr.lastIndexOf("/") + 1);

          langLinkInfo.setArticleTitle(langLinkFar.getArticleTitle());
          langLinkInfo.setArticleName(articleName);
          langLinkInfo.setArticleUrl(articleUrlStr);
          langLinkInfo.setLangCode(langLinkFar.getLangCode());
          langLinkInfo.setLangName(langLinkFar.getLangName());
          langLinkInfo.setLangAutonym(langLinkFar.getLangAutonym());

          langLinkInfoList.add(langLinkInfo);
        }
      }
      else {
        log.info("no pages in langlinks results");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling langlinks service " + e);
    }

    return langLinksResponseNear;
  }
}
