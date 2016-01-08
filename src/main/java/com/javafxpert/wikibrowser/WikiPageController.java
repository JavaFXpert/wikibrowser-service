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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by jamesweaver on 11/29/15.
 */
@RestController
@EnableAutoConfiguration
public class WikiPageController {
  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiPageController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }

  /**
   *
   */
  @RequestMapping("/wikipage")
  String generateWikiBrowserPage(@RequestParam(value = "name", defaultValue="Ada_Lovelace")
              String wikipediaPageName,
              @RequestParam(value = "lang")
              String lang) throws Exception {

    String language = wikiBrowserProperties.computeLang(lang);

    String mWikipediaBase = String.format(WikiClaimsController.WIKIPEDIA_MOBILE_BASE_TEMPLATE, language);
    String mWikipedia = String.format(WikiClaimsController.WIKIPEDIA_MOBILE_TEMPLATE, language);
    String mWikipediaPage = mWikipedia + wikipediaPageName;

    URL url = new URL(mWikipediaPage);
    InputStream inputStream = url.openStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

    String pageText = reader.lines().collect(Collectors.joining("\n"));
    pageText = pageText.replaceAll("\\\"/w/", "\"" + mWikipediaBase + "w/");
    pageText = pageText.replaceAll("\\\"/images/", "\"" + mWikipediaBase + "images/");
    pageText = pageText.replaceAll("\\\"/static/", "\"" + mWikipediaBase + "static/");

    pageText = pageText.replaceAll("\\\"href\\\":\\\"/w/", "\"href\":\"" + mWikipediaBase + "w/");
    pageText = pageText.replaceAll("\\\"href\\\":\\\"/wiki/", "\"href\":\"" + mWikipedia);

    pageText = pageText.replaceAll("href=\\\"/wiki/", "href=\"" + "/wikipage?lang=" + language + "&name=");

    pageText = pageText.replaceAll("href=\\\"//", "target=\"_blank\" href=\"//");

    pageText = pageText.replaceAll("href=\\\"http", "target=\"_blank\" href=\"http");

    // Remove the search menu at the top of the page
    int searchAnchorStartPos = pageText.indexOf("<a title");
    if (searchAnchorStartPos > 0) {
      int searchAnchorEndPos = pageText.indexOf("</a>", searchAnchorStartPos);
      if (searchAnchorEndPos > 0) {
        pageText = pageText.substring(0, searchAnchorStartPos) + pageText.substring(searchAnchorEndPos + "</a>".length());
      }
    }

    // Remove the search form at the top of the page
    int searchFormStartPos = pageText.indexOf("<form");
    if (searchFormStartPos > 0) {
      int searchFormEndPos = pageText.indexOf("</form>", searchFormStartPos);
      if (searchFormEndPos > 0) {
        pageText = pageText.substring(0, searchFormStartPos) + pageText.substring(searchFormEndPos + "</form>".length());
      }
    }

    return pageText;
  }
}
