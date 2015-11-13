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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by jamesweaver on 10/13/15.
 */
public class MusicChord {
  private final String root;
  private final String chordType;
  private final String bassNote;
  private final int inversion;
  private final boolean isMajor;
  private final boolean isMinor;
  private final String name;

  public MusicChord(String root, String chordType, String bassNote, int inversion, boolean isMajor, boolean isMinor, String name) {
    this.root = root;
    this.chordType = chordType;
    this.bassNote = bassNote;
    this.inversion = inversion;
    this.isMajor = isMajor;
    this.isMinor = isMinor;
    this.name = name;
  }

  public String getRoot() {
    return root;
  }

  public String getChordType() {
    return chordType;
  }

  public String getBassNote() {
    return bassNote;
  }

  public int getInversion() {
    return inversion;
  }

  public boolean isMajor() {
    return isMajor;
  }

  public boolean isMinor() {
    return isMinor;
  }

  public String getName() {
    return name;
  }
}
