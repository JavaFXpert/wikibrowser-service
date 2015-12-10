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

package com.javafxpert.wikibrowser.model.language;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James Weaver
 */
@JsonRootName("languageResults")
public class LangLinksResponseNear {
  @JsonProperty("langlinks")
  private List<LangLinkInfo> langLinkInfoList = new ArrayList<>();

  public LangLinksResponseNear(List<LangLinkInfo> langLinkInfoList) {
    this.langLinkInfoList = langLinkInfoList;
  }

  public LangLinksResponseNear() {
  }

  public List<LangLinkInfo> getLangLinkInfoList() {
    return langLinkInfoList;
  }

  public void setLangLinkInfoList(List<LangLinkInfo> langLinkInfoList) {
    this.langLinkInfoList = langLinkInfoList;
  }

  @Override
  public String toString() {
    return "LangLinksResponseNear{" +
        "langLinkInfoList=" + langLinkInfoList +
        '}';
  }
}

