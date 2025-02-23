package org.aturkov.expense.controller.simple.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.TemplateCreateRqDTOv1;
import org.aturkov.expense.mapper.deposit.DepositDTOMapper;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.mapper.template.TemplateCreateDTOReverseMapper;
import org.aturkov.expense.mapper.template.TemplateSaveDTOReverseMapper;
import org.aturkov.expense.mapper.template.TemplateDTOMapperV2;
import org.aturkov.expense.service.deposit.DepositService;
import org.aturkov.expense.service.item.ItemService;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class TemplateCreateController {
    private final TemplateService templateService;
    private final TemplateDTOMapperV2 templateDTOMapperV2;
    private final DepositDTOMapper depositDTOMapper;
    private final TemplateCreateDTOReverseMapper templateCreateDTOReverseMapper;
    private final DepositService depositService;
    private final ItemService itemService;
    private final ItemDTOMapper itemDTOMapper;

    @GetMapping("/template/create-form")
    public String showTemplateCreateForm(
            Model model) {
        try {
            model.addAttribute("template", new TemplateCreateRqDTOv1());
            model.addAttribute("templates", templateDTOMapperV2.convertCollection(templateService.getIncomeTemplate()));
            model.addAttribute("deposits", depositDTOMapper.convertCollection(depositService.findDeposits()));
            model.addAttribute("items", itemDTOMapper.convertCollection(itemService.findItems()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/template/create-form";
    }

    @PostMapping("/template/save")
    public String createTemplate(
            @ModelAttribute("template") TemplateCreateRqDTOv1 request,
            RedirectAttributes redirectAttributes) {
        TemplateEntity entity;
        try {
            entity = templateCreateDTOReverseMapper.convert(request.getTemplate());
            entity = templateService.createTemplate(entity);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
            return "redirect:/template/create-form";
        }
        return "redirect:/template/card/" + entity.getId();
    }
}
