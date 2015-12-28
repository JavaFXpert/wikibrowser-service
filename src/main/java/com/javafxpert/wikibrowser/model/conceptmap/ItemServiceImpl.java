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
