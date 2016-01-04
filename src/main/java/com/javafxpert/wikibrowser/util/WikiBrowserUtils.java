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
}
