package com.javafxpert.wikibrowser.model.bitly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.javafxpert.wikibrowser.model.conceptmap.GraphLinkNear;
import com.javafxpert.wikibrowser.model.conceptmap.GraphNodeNear;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonRootName("bitly")
@JsonPropertyOrder({"status_code", "status_txt", "long_url", "url"})

@JsonIgnoreProperties(ignoreUnknown = true)
public class BitlyResponseNear implements Serializable {
  @JsonProperty("status_code")
  private int statusCode;

  @JsonProperty("status_txt")
  private String statusTxt;

  @JsonProperty("long_url")
  private String longUrl;

  @JsonProperty("url")
  private String url;

  public BitlyResponseNear() {
  }

  public BitlyResponseNear(int statusCode, String statusTxt, String longUrl, String url) {
    this.statusCode = statusCode;
    this.statusTxt = statusTxt;
    this.longUrl = longUrl;
    this.url = url;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusTxt() {
    return statusTxt;
  }

  public void setStatusTxt(String statusTxt) {
    this.statusTxt = statusTxt;
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
    return "BitlyResponseNear{" +
        "statusCode=" + statusCode +
        ", statusTxt='" + statusTxt + '\'' +
        ", longUrl='" + longUrl + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
