package org.aturkov.expense.controller.simple.itemgroup;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dto.item.ItemCreateRqDTOv1;
import org.aturkov.expense.dto.item.ItemRsListDTOv1;
import org.aturkov.expense.mapper.item.ItemCreateDTOReverseMapper;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.service.item.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class ItemGroupController {
    private final ItemService itemService;
    private final ItemDTOMapper itemDTOMapper;
    private final ItemCreateDTOReverseMapper itemCreateDTOReverseMapper;

    @GetMapping("/item_group/list")
    public String showItemGroups(
            RedirectAttributes redirectAttributes,
            Model model) {
        ItemRsListDTOv1 rs = new ItemRsListDTOv1();
        try {
            List<ItemEntity> entityList = itemService.findItems();
            rs
                    .setItems(itemDTOMapper.convertCollection(entityList));
            model.addAttribute("rs", rs);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/item/list";
        }
        return "/item/list";
    }

//    @GetMapping("/item/create-form")
//    public String showItemCreateForm(
//            Model model) {
//        try {
//            model.addAttribute("rs", new ItemCreateRqDTOv1());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return "/item/create-form";
//    }
//
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
