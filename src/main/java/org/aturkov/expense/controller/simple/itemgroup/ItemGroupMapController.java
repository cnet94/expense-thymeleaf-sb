package org.aturkov.expense.controller.simple.itemgroup;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.itemgroup.ItemGroupMapEntity;
import org.aturkov.expense.dto.item.ItemCreateRqDTOv1;
import org.aturkov.expense.dto.itemgroup.ItemGroupMapCreateRqDTOv1;
import org.aturkov.expense.dto.itemgroup.ItemGroupMapRsListDTOv1;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.mapper.itemgroup.ItemGroupDTOMapper;
import org.aturkov.expense.mapper.itemgroup.ItemGroupMapDTOMapper;
import org.aturkov.expense.mapper.itemgroup.ItemGroupMapDTOMapperV2;
import org.aturkov.expense.service.item.ItemGroupMapService;
import org.aturkov.expense.service.item.ItemGroupService;
import org.aturkov.expense.service.item.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ItemGroupMapController {
    private final ItemGroupMapDTOMapperV2 itemGroupMapDTOMapperV2;
    private final ItemGroupMapService itemGroupMapService;
    private final ItemDTOMapper itemDTOMapper;
    private final ItemService itemService;
    private final ItemGroupDTOMapper itemGroupDTOMapper;
    private final ItemGroupService itemGroupService;

    @GetMapping("/item_group_map/list")
    public String showItemGroupMaps(
            RedirectAttributes redirectAttributes,
            Model model) {
        ItemGroupMapRsListDTOv1 rs = new ItemGroupMapRsListDTOv1();
        try {
            List<ItemGroupMapEntity> list = itemGroupMapService.findItemGroupMaps();
            rs
                    .setItemGroupMaps(itemGroupMapDTOMapperV2.convertCollection(list));
            model.addAttribute("rs", rs);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/item_group_map/list";
        }
        return "/item_group_map/list";
    }

    @GetMapping("/item_group_map/create-form")
    public String showItemGroupMapCreateForm(
            Model model) {
        try {
            model.addAttribute("rs", new ItemGroupMapCreateRqDTOv1());
            model.addAttribute("itemGroups", itemGroupDTOMapper.convertCollection(itemGroupService.findItemGroups()));
            model.addAttribute("items", itemDTOMapper.convertCollection(itemService.findItems()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/item_group_map/create-form";
    }

//    @PostMapping("/item/save")
//    public String createItem(
//            RedirectAttributes redirectAttributes,
//            @ModelAttribute("item") ItemCreateRqDTOv1 request) {
//        ItemEntity entity;
//        try {
//            entity = itemCreateDTOReverseMapper.convert(request.getItem());
//            itemService.createItem(entity);
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
//            return "redirect:/item/create-form";
//        }
//        return "redirect:/item/list";
//    }
//
//    @PostMapping("/item/delete/{deleteId}")
//    public String deleteItem(
//            RedirectAttributes redirectAttributes,
//            @PathVariable("deleteId") UUID deleteId) {
//        try {
//            itemService.deleteItem(deleteId);
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
//            return "redirect:/item/list";
//        }
//        return "redirect:/item/list";
//    }
}
