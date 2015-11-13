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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by jamesweaver on 10/13/15.
 */
@RestController
public class WikiBrowserController {


  private MusicChord musicChord;
  @RequestMapping("/analyze")
  public ResponseEntity<Object> identifyChordByNotes(@RequestParam(value = "notes") String notes) {
    musicChord = null;
    try {
      musicChord = new MusicChord("C",
                                  "maj",
                                  "G",
                                  2,
                                  true,
                                  false,
                                  "fred");
    }
    catch (Exception e) {
      System.out.println("Exception encountered in WikiBrowserController#identifyChordByNotes: " + e);
    }
    return Optional.ofNullable(musicChord)
        .map(mc -> new ResponseEntity<>((Object)mc, HttpStatus.OK))
        .orElse(new ResponseEntity<>("Could not analyze the chord", HttpStatus.INTERNAL_SERVER_ERROR));

  }
}

