package org.aturkov.expense.controller.simple.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class TemplateDeleteController {
    private final TemplateService templateService;

    @PostMapping("/template/{id}/delete")
    public String deleteTemplate(
            @PathVariable("id") UUID id,
            RedirectAttributes redirectAttributes) {
        templateService.deleteTemplate(id);
        redirectAttributes.addFlashAttribute("successMessage", "Message: template perhaps was deleted successfully");
        return "redirect:/template/list";
    }

    @PostMapping("/template/{id}/archive")
    public String archiveTemplate(
            @PathVariable("id") UUID id) {
        templateService.archiveTemplate(id);
        return "redirect:/template/list";
    }
}
