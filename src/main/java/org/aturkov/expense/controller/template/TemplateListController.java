package org.aturkov.expense.controller.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dto.template.TemplateRsDTOv1;
import org.aturkov.expense.mapper.template.TemplateDTOMapper;
import org.aturkov.expense.service.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class TemplateListController {
    private final TemplateService templateService;
    private final TemplateDTOMapper templateDTOMapper;

    @GetMapping("/template/list")
    public String showTemplates(Model model) {
        List<TemplateRsDTOv1> rs;
        try {
            rs = templateDTOMapper.convertCollection(templateService.getAllThatIsActive());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("templateList", rs);
        return "/template/list";
    }
}
