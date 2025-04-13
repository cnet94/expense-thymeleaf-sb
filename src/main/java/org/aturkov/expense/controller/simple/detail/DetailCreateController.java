package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.OperationType;
import org.aturkov.expense.dto.detail.ExpenseDetailCreateDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailCreateRqDTOv1;
import org.aturkov.expense.mapper.detail.ExpenseDetailCreateDTOReverseMapper;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.service.detail.DetailService;
import org.aturkov.expense.service.item.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DetailCreateController {
    private final DetailService detailService;
    private final ExpenseDetailCreateDTOReverseMapper expenseDetailCreateDTOReverseMapper;
    private final ItemDTOMapper itemDTOMapper;
    private final ItemService itemService;

    @GetMapping("/detail/create-form")
    public String showDetailCreateForm(
            Model model) {
        ExpenseDetailCreateRqDTOv1 rs = new ExpenseDetailCreateRqDTOv1();
        try {
            model.addAttribute("detail", rs.setDetail(new ExpenseDetailCreateDTOv1()));
            model.addAttribute("items", itemDTOMapper.convertCollection(itemService.findItems()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/detail/create-form";
    }

    @GetMapping("/detail/template/{id}/create-form")
    public String showDetailCreateFormForTemplate(
            @PathVariable("id") UUID templateId,
            @RequestParam("name") String name,
            @RequestParam("currency") CurrencyType currency,
            @RequestParam("operationType") OperationType operationType,
            Model model) {
        ExpenseDetailCreateRqDTOv1 rs = new ExpenseDetailCreateRqDTOv1();
        rs.setDetail(
                (ExpenseDetailCreateDTOv1) new ExpenseDetailCreateDTOv1()
                        .setTemplateId(templateId)
                        .setName(name)
                        .setCurrency(currency)
                        .setOperationType(operationType)
        );
        model.addAttribute("rs", rs);
        return "/detail/create-simple-form";
    }

    @PostMapping("/detail/save")
    public String saveDetail(
            @ModelAttribute("template") ExpenseDetailCreateRqDTOv1 request) {
        try {
            ExpenseDetailEntity detail = expenseDetailCreateDTOReverseMapper.convert(request.getDetail());
            detailService.createSimpleDetail(detail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/detail/list";
    }

    @PostMapping("/detail/template/{id}/save")
    public String saveDetailByTemplate(
            @PathVariable("id") UUID templateId,
            ExpenseDetailCreateRqDTOv1 request,
            RedirectAttributes redirectAttributes) {
        try {
            ExpenseDetailEntity entity = expenseDetailCreateDTOReverseMapper.convert(request.getDetail());
            entity.setTemplateId(templateId);
            detailService.createSimpleDetail(entity);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
        }
        return "redirect:/template/" + templateId + "/card";
    }

    @PostMapping("/detail/simple/save")
    public String saveSimpleDetail(
            @ModelAttribute ExpenseDetailCreateRqDTOv1 request) {
        try {
            ExpenseDetailEntity detail = expenseDetailCreateDTOReverseMapper.convert(request.getDetail());
            detailService.createSimpleDetail(detail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/detail/list";
    }
}
