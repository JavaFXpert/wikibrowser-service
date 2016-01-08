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
package com.javafxpert.wikibrowser.model.conceptmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jamesweaver on 12/28/15.
 */
@Service
public class ItemServiceImpl implements ItemService {
  private ItemRepository itemRepository;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  };

  public ItemRepository getItemRepository() {
    return itemRepository;
  }

  public void setItemRepository(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Override
  public String toString() {
    return "ItemServiceImpl{" +
        "itemRepository=" + itemRepository +
        '}';
  }
}
