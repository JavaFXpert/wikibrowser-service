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
    pageText = pageText.replaceAll("\\\"/images/", "\"" + mWikipediaBase + "images/");

    pageText = pageText.replaceAll("\\\"href\\\":\\\"/w/", "\"href\":\"" + mWikipediaBase + "w/");
    pageText = pageText.replaceAll("\\\"href\\\":\\\"/wiki/", "\"href\":\"" + mWikipedia);

    pageText = pageText.replaceAll("href=\\\"/wiki/", "href=\"" + "/wikipage?lang=" + language + "&name=");
    StringBuilder pageTextSb = new StringBuilder(pageText);
    boolean finished = false;
    int prevFoundPos = 0;
    String matchStr = "/wikipage?name=";
    String templateStr = " onclick='window.parent.refreshClaims(\"%s\", \"en\")' ";
    while (!finished) {
      int curFoundPos = pageTextSb.indexOf("/wikipage?name=", prevFoundPos + matchStr.length());
      if (curFoundPos > prevFoundPos) {
        prevFoundPos = curFoundPos;
        int nameStartPos = curFoundPos + matchStr.length();
        int nameEndPosExc = pageTextSb.indexOf("\"", nameStartPos + 1);
        if (nameEndPosExc > nameStartPos) {
          String articleName = pageTextSb.substring(nameStartPos, nameEndPosExc);
          String.format(templateStr, articleName);
          pageTextSb.insert(nameEndPosExc + 1, String.format(templateStr, articleName));
        }
        else {
          finished = true;
        }
      }
      else {
        finished = true;
      }
    }
    return pageTextSb.toString();
  }
}
