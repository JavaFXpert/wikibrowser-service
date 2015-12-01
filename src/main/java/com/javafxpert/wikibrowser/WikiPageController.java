package com.javafxpert.wikibrowser;

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

  /**
   *
   */
  @RequestMapping("/wikipage")
  String generateWikiBrowserPage(@RequestParam(value = "name", defaultValue="Ada_Lovelace")
              String wikipediaPageName,
              @RequestParam(value = "lang", defaultValue="en")
              String lang) throws Exception {
    String mWikipediaBase = String.format(WikiClaimsController.WIKIPEDIA_MOBILE_BASE_TEMPLATE, lang);
    String mWikipedia = String.format(WikiClaimsController.WIKIPEDIA_MOBILE_TEMPLATE, lang);
    String mWikipediaPage = mWikipedia + wikipediaPageName;

    URL url = new URL(mWikipediaPage);
    InputStream inputStream = url.openStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

    /*
    StringBuilder response = new StringBuilder();
    String inputLine;
    while ((inputLine = reader.readLine()) != null)
      response.append(inputLine);
    reader.close();
    return response.toString();
    */

    String pageText = reader.lines().collect(Collectors.joining("\n"));
    pageText = pageText.replaceAll("\\\"/w/", "\"" + mWikipediaBase + "w/");
    pageText = pageText.replaceAll("\\\"href\\\":\\\"/w/", "\"href\":\"" + mWikipediaBase + "w/");
    pageText = pageText.replaceAll("\\\"href\\\":\\\"/wiki/", "\"href\":\"" + mWikipedia);
    pageText = pageText.replaceAll("href=\\\"/wiki/", "href=\"" + "/wikipage?name=");
    //pageText = pageText.replaceAll("href=\\\"/wiki/", "href=\"" + mWikipedia);
    log.info(pageText);
    return pageText;
    //return "<h1>Hello World!<br/>" + mWikipedia + "</h1>";
  }
}
