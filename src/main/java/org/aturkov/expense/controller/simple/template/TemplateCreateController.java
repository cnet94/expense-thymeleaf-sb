package org.aturkov.expense.controller.simple.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.TemplateCreateDTOv1;
import org.aturkov.expense.mapper.deposit.DepositDTOMapper;
import org.aturkov.expense.mapper.template.TemplateCreateDTOReverseMapper;
import org.aturkov.expense.mapper.template.TemplateDTOMapperV2;
import org.aturkov.expense.service.other.DepositService;
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

    @GetMapping("/template/add/form")
    public String showTemplateForm(Model model) {
        try {
            model.addAttribute("template", new TemplateCreateDTOv1());
            model.addAttribute("templates", templateDTOMapperV2.convertCollection(templateService.getIncomeTemplate()));
            model.addAttribute("deposits", depositDTOMapper.convertCollection(depositService.getDeposits()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/template/add/form";
    }

    @PostMapping("/template/save")
    public String createTemplate(
            @ModelAttribute("template") TemplateCreateDTOv1 request,
            RedirectAttributes redirectAttributes) {
        TemplateEntity templateEntity;
        try {
            templateEntity = templateService.createTemplate(templateCreateDTOReverseMapper.convert(request));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
            return "redirect:/template/add/form";
        }
        return "redirect:/template/card/" + templateEntity.getId();
    }
}
