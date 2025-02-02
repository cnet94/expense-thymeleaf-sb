package org.aturkov.expense.controller.rest;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dto.template.TemplateRsDTOv1;
import org.aturkov.expense.mapper.template.TemplateDTOMapper;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/rest")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TemplateRestController {
    private final TemplateService templateService;
    private final TemplateDTOMapper templateDTOMapper;

    @GetMapping(value = "/private/template/list/v1")
    public List<TemplateRsDTOv1> templateList() {
        List<TemplateRsDTOv1> list;
        try {
            list = templateDTOMapper.convertCollection(templateService.getAllThatIsActive());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
