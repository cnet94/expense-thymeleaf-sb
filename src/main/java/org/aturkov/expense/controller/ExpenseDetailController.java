package org.aturkov.expense.controller;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dto.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.expense.ExpenseDetailDTOMapper;
import org.aturkov.expense.mapper.expense.ExpenseDetailDTOReverseMapper;
import org.aturkov.expense.service.ExpenseDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class ExpenseDetailController {
    private final ExpenseDetailService expenseDetailService;
    private final ExpenseDetailDTOMapper expenseDetailDTOMapper;
    private final ExpenseDetailDTOReverseMapper expenseDetailDTOReverseMapper;

    @GetMapping("/detail/list")
    public String showExpenseDetailsList(Model model) {
        List<ExpenseDetailDTOv1> expenseDetailsList = new ArrayList<>();
        expenseDetailService.getExpenseDetailsForCurrentMonth().forEach(expenseDetail -> expenseDetailsList.add(expenseDetailDTOMapper.map(expenseDetail)));
        model.addAttribute("expenseDetails", expenseDetailsList);
        return "/detail/list";
    }

    @GetMapping("/detail/edit/{id}")
    public String showUpdateForm(@PathVariable("id") UUID detailId, Model model) {
        ExpenseDetailDTOv1 expenseDetail = expenseDetailDTOMapper.map(expenseDetailService.getExpenseDetailById(detailId));
        model.addAttribute("expenseDetail", expenseDetail);
        return "/detail/card";
    }

    @PostMapping("/detail/update")
    public String updateExpenseDetail(
            ExpenseDetailDTOv1 expenseDetail) {
        expenseDetailService.updateExpenseDetail(expenseDetailDTOReverseMapper.map(expenseDetail));
        return "redirect:/expense/list";
    }

    @PostMapping("/detail/approve")
    public String uploadImage(
            @RequestParam("id") UUID expenseId,
            @RequestParam("detailId") UUID detailId,
            @RequestParam("image") MultipartFile file) throws IOException {
        expenseDetailService.approvePaymentExpenseDetail(expenseId, detailId, file);
        return "redirect:/expense/card/" + expenseId;
    }

    @PostMapping("/detail/cancel")
    public String updateExpenseDetailCancel(
            @RequestParam("id") UUID expenseId,
            @RequestParam("detailId") UUID detailId) {
        expenseDetailService.expenseDetailCancelPayment(detailId);
        return "redirect:/expense/card/" + expenseId;
    }

    @PostMapping("/detail/delete")
    public String updateExpenseDetailDelete(
            @RequestParam("id") UUID expenseId,
            @RequestParam("detailId") UUID detailId) {
        expenseDetailService.expenseDetailDelete(detailId);
        return "redirect:/expense/card/" + expenseId;
    }

}
