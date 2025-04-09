package org.aturkov.expense.controller.simple.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dto.template.TemplateRsDTOv1;
import org.aturkov.expense.mapper.deposit.DepositDTOMapper;
import org.aturkov.expense.mapper.template.TemplateDTOMapper;
import org.aturkov.expense.service.deposit.DepositService;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class TemplateViewController {
    private final TemplateService templateService;
    private final TemplateDTOMapper templateDTOMapper;
    private final DepositService depositService;
    private final DepositDTOMapper depositDTOMapper;

    @PostMapping("/template/card")
    public String getTemplate(@RequestParam("detailId") UUID detailId, Model model) {
        TemplateRsDTOv1 rs;
        try {
            rs = templateDTOMapper.convert(templateService.getTemplateByDetailIdWithSort(detailId));
            model.addAttribute("template", rs);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "error message");
        }
        return "/template/card";
    }

    @GetMapping("/template/{id}/card")
    public String getExpenseDetails(
            @PathVariable("id") UUID id,
            Model model) {
        TemplateRsDTOv1 rs;
        try {
            rs = templateDTOMapper.convert(templateService.getTemplateWithSortDetail(id));
            model.addAttribute("template", rs);
            model.addAttribute("deposits", depositDTOMapper.convertCollection(depositService.findDeposits()));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "error message");
        }
        return "/template/card";
    }
}
