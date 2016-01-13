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

import com.javafxpert.wikibrowser.model.bitly.BitlyDataFar;
import com.javafxpert.wikibrowser.model.bitly.BitlyResponseFar;
import com.javafxpert.wikibrowser.model.bitly.BitlyResponseNear;
import com.javafxpert.wikibrowser.model.conceptmap.*;
import com.javafxpert.wikibrowser.model.search.SearchFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseFar;
import com.javafxpert.wikibrowser.model.search.SearchResponseNear;
import com.javafxpert.wikibrowser.model.search.SearchinfoFar;
import com.javafxpert.wikibrowser.util.WikiBrowserUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
@RequestMapping("/bitly")
public class WikiBitlyController {
  private Log log = LogFactory.getLog(getClass());

  private final WikiBrowserProperties wikiBrowserProperties;

  @Autowired
  public WikiBitlyController(WikiBrowserProperties wikiBrowserProperties) {
    this.wikiBrowserProperties = wikiBrowserProperties;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> search(@RequestParam(value = "items", defaultValue="") String items,
                                       @RequestParam(value = "lang") String lang) {
    // Example endpoint usage is bitly?items=Q24, Q30, Q23, Q16, Q20&lang=fr
    // Scrub the input, and output a string for the Bitly service similar to the following:
    // Q24,Q30,Q23,Q16,Q20
    String argStr = WikiBrowserUtils.scrubItemIds(items, false);
    log.info("argStr=" + argStr);

    BitlyResponseNear bitlyResponseNear = null;

    if (argStr.length() > 0) {
      String blat = wikiBrowserProperties.getBlat();
      String bitlyUrlPart = "https://api-ssl.bitly.com/v3/shorten?access_token=" + blat + "&longUrl=";

      String conceptMapUrlPart = null;

      try {
        conceptMapUrlPart = URLEncoder.encode("http://conceptmap.io?items=" + argStr + "&lang=" + lang, "UTF-8");
      }
      catch (UnsupportedEncodingException uee) {
        log.error("Exception whild encoding for bitly");
      }

      bitlyResponseNear = bitlyServiceResponse(bitlyUrlPart + conceptMapUrlPart);
    }
    else {
      log.warn("Invalid argument to WikiBitlyController /bitly endpoint: \"" + items + "\"");
    }

    return Optional.ofNullable(bitlyResponseNear)
        .map(cr -> new ResponseEntity<>((Object)cr, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Bitly request unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  /**
   * Calls the Bitly URL shortener service and returns an object that holds results
   * @param bitlyRequestUrl
   * @return
   */
  private BitlyResponseNear bitlyServiceResponse(String bitlyRequestUrl) {
    log.info("bitlyRequestUrl: " + bitlyRequestUrl);

    BitlyResponseFar bitlyResponseFar = null;
    BitlyResponseNear bitlyResponseNear = new BitlyResponseNear();
    try {
      bitlyResponseFar = new RestTemplate().getForObject(new URI(bitlyRequestUrl),
          BitlyResponseFar.class);
      log.info("bitlyResponseFar: " + bitlyResponseFar.toString());

      int statusCode = bitlyResponseFar.getStatusCode();
      if (statusCode == 200) {
        bitlyResponseNear.setStatusCode(bitlyResponseFar.getStatusCode());
        bitlyResponseNear.setStatusTxt(bitlyResponseFar.getStatusTxt());
        bitlyResponseNear.setLongUrl(bitlyResponseFar.getBitlyDataFar().getLongUrl());
        bitlyResponseNear.setUrl(bitlyResponseFar.getBitlyDataFar().getUrl());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      log.info("Caught exception when calling Bitly URL shortener service " + e);
    }

    return bitlyResponseNear;
  }
}
