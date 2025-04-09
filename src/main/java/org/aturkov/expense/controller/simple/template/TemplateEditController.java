package org.aturkov.expense.controller.simple.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dto.template.TemplateRsDTOv1;
import org.aturkov.expense.mapper.template.TemplateDTOMapper;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class TemplateEditController {
    private final TemplateService templateService;
    private final TemplateDTOMapper templateDTOMapper;

    @GetMapping("/template/{id}/edit")
    public String showUpdateTemplateForm(
            @PathVariable("id") UUID id,
            Model model,
            RedirectAttributes redirectAttributes) {
        TemplateRsDTOv1 template;
        try {
            template = templateDTOMapper.convert(templateService.getTemplateWithSortDetail(id));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
            return "redirect:/template/list";
        }
        model.addAttribute("template", template);
        return "template/update-form";
    }
}
