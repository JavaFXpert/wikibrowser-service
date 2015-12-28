package com.javafxpert.wikibrowser.model.conceptmap;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jamesweaver on 12/28/15.
 */
public class ItemServiceImpl implements ItemService {
  private ItemRepository itemRepository;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  };
}
