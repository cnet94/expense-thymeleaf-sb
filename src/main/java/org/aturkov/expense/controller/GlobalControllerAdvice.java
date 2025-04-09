package org.aturkov.expense.controller;

import org.aturkov.expense.dto.item.ItemDTOv1;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.service.item.ItemService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final ItemService itemService;
    private final ItemDTOMapper itemDTOMapper;

    public GlobalControllerAdvice(ItemService itemService, ItemDTOMapper itemDTOMapper) {
        this.itemService = itemService;
        this.itemDTOMapper = itemDTOMapper;
    }

    @ModelAttribute("items")
    public List<ItemDTOv1> populateItems() throws Exception {
        return itemDTOMapper.convertCollection(itemService.findItems());
    }
}