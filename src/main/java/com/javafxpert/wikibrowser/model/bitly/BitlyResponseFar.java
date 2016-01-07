package com.javafxpert.wikibrowser.model.bitly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javafxpert.wikibrowser.model.conceptmap.ResultFar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesweaver on 1/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitlyResponseFar implements Serializable {
  @JsonProperty("status_code")
  private int statusCode;

  @JsonProperty("status_txt")
  private String statusTxt;

  @JsonProperty("data")
  private BitlyDataFar bitlyDataFar;

  public BitlyResponseFar() {
  }

  public BitlyResponseFar(int statusCode, String statusTxt, BitlyDataFar bitlyDataFar) {
    this.statusCode = statusCode;
    this.statusTxt = statusTxt;
    this.bitlyDataFar = bitlyDataFar;
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

  public BitlyDataFar getBitlyDataFar() {
    return bitlyDataFar;
  }

  public void setBitlyDataFar(BitlyDataFar bitlyDataFar) {
    this.bitlyDataFar = bitlyDataFar;
  }

  @Override
  public String toString() {
    return "BitlyResponseFar{" +
        "statusCode=" + statusCode +
        ", statusTxt='" + statusTxt + '\'' +
        ", bitlyDataFar=" + bitlyDataFar +
        '}';
  }
}
