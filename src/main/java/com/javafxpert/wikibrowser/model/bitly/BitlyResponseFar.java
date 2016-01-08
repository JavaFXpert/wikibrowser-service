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
