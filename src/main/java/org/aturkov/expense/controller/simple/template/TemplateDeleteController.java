package org.aturkov.expense.controller.simple.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class TemplateDeleteController {
    private final TemplateService templateService;

    @PostMapping("/template/delete/{id}")
    public String deleteTemplate(@PathVariable("id") UUID id) {
        templateService.deleteTemplate(id);
        return "redirect:/template/list";
    }

    @PostMapping("/template/archive/{id}")
    public String archiveTemplate(@PathVariable("id") UUID id) {
        templateService.archiveTemplate(id);
        return "redirect:/template/list";
    }
}
