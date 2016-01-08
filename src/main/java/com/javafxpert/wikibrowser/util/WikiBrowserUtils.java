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
package com.javafxpert.wikibrowser.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import java.nio.charset.Charset;

/**
 * Created by jamesweaver on 1/4/16.
 */
public class WikiBrowserUtils {
  /**
   * TODO: Move this to a util class
   * @param username
   * @param password
   * @return
   */
  public static HttpHeaders createHeaders(final String username, final String password ){
    HttpHeaders headers =  new HttpHeaders(){
      {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
            auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        set( "Authorization", authHeader );
      }
    };
    headers.add("Content-Type", "application/xml");
    headers.add("Accept", "application/xml");

    return headers;
  }

  /**
   * Scrub the list of item IDs input, which could appear like the following:
   * Q24, q30,Q23, Q16, Q20
   * and output a string similar to the following:
   * 'Q24','Q30','Q23','Q16','Q20'
   * Note that quotes are output if only if specified
   */
  public static String scrubItemIds(String items, boolean insertQuotes) {
    String[] itemsArray = items.split(",");
    String argStr = "";
    for (int i = 0; i < itemsArray.length; i++) {
      String itemStr = itemsArray[i];
      itemStr = itemStr.trim().toUpperCase();
      if (itemStr.length() > 0 && itemStr.substring(0, 1).equals("Q")) {
        if (insertQuotes) {
          argStr += "'" + itemStr + "'";
        }
        else {
          argStr += itemStr;
        }
        if (i < itemsArray.length - 1) {
          argStr += ",";
        }
      }
    }
    return argStr;
  }
}
