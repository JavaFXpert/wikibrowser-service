package com.javafxpert.wikibrowser.model.bitly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javafxpert.wikibrowser.model.conceptmap.GraphFar;

import java.io.Serializable;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitlyDataFar implements Serializable {
  @JsonProperty("long_url")
  private String longUrl;

  @JsonProperty("url")
  private String url;

  public BitlyDataFar() {
  }

  public BitlyDataFar(String longUrl, String url) {
    this.longUrl = longUrl;
    this.url = url;
  }

  public String getLongUrl() {
    return longUrl;
  }

  public void setLongUrl(String longUrl) {
    this.longUrl = longUrl;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "BitlyDataFar{" +
        "longUrl='" + longUrl + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
