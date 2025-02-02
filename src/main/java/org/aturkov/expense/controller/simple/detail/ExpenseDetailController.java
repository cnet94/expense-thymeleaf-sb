package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailSaveRqDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailUpdateRqDTOv1;
import org.aturkov.expense.mapper.detail.ExpenseDetailDTOMapper;
import org.aturkov.expense.mapper.detail.ExpenseDetailSaveDTOReverseMapper;
import org.aturkov.expense.mapper.detail.ExpenseDetailUpdateDTOReverseMapper;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class ExpenseDetailController {
    private final DetailService detailService;
    private final ExpenseDetailDTOMapper expenseDetailDTOMapper;
    private final ExpenseDetailSaveDTOReverseMapper expenseDetailSaveDTOReverseMapper;
    private final ExpenseDetailUpdateDTOReverseMapper expenseDetailUpdateDTOReverseMapper;

    @GetMapping("/detail/list")
    public String showExpenseDetailsList(Model model) {
        List<ExpenseDetailDTOv1> list;
        try {
            list = expenseDetailDTOMapper.convertCollection(detailService.getExpenseDetailsForCurrentMonth());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("expenseDetails", list);
        return "/detail/list";
    }

    @GetMapping("/detail/add")
    public String showAddExpenseDetailForm(
            @RequestParam("templateId") UUID templateId,
            @RequestParam("name") String name,
            Model model) {
        model.addAttribute("expenseDetail",
                new ExpenseDetailDTOv1()
                        .setTemplateId(templateId)
                        .setName(name));
        return "/detail/create/card";
    }

    @PostMapping("/detail/add")
    public String createExpenseDetail(@ModelAttribute("expenseDetail") ExpenseDetailSaveRqDTOv1 detail) {
        try {
            detailService.createExpenseDetail(expenseDetailSaveDTOReverseMapper.convert(detail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/card/" + detail.getTemplateId();
    }

    @GetMapping("/detail/edit/{detailId}")
    public String showUpdateForm(
            @PathVariable("detailId") UUID detailId,
            Model model) {
        ExpenseDetailDTOv1 expenseDetail;
        try {
            expenseDetail = expenseDetailDTOMapper.convert(detailService.findExpenseDetail(detailId));
            model.addAttribute("expenseDetail", expenseDetail);
        } catch (ServiceException e) {
            model.addAttribute("errorMessage", "Error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/detail/update/card";
    }

    @PostMapping("/detail/save")
    public String saveExpenseDetail(
            ExpenseDetailSaveRqDTOv1 request) {
        try {
            ExpenseDetailEntity detailForSave = expenseDetailSaveDTOReverseMapper.convert(request);
            detailService.createExpenseDetail(detailForSave);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/card/" + request.getTemplateId();
    }

    @PostMapping("/detail/update")
    public String updateExpenseDetail(
            ExpenseDetailUpdateRqDTOv1 request) {
        try {
            ExpenseDetailEntity detail = expenseDetailUpdateDTOReverseMapper.convert(request);
            detailService.updateExpenseDetail(detail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/card/" + request.getTemplateId();
    }

    @PostMapping("/detail/delete/{detailId}")
    public String deleteExpenseDetail(
            @RequestParam("templateId") UUID templateId,
            @PathVariable("detailId") UUID detailId) {
        detailService.deleteExpenseDetail(detailId);
        return "redirect:/template/card/" + templateId;
    }

    @PostMapping("/detail/payment/approve/{detailId}")
    public String approveDetailPaymentV1(
            @PathVariable("detailId") UUID detailId,
            @RequestParam("image") MultipartFile file) {
        ExpenseDetailDTOv1 ret;
        try {
            ret = expenseDetailDTOMapper.convert(detailService.approvePaymentDetail(detailId, file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (ret.getTemplateId() == null)
            return "redirect:/detail/list";
        else
            return "redirect:/template/card/" + ret.getTemplateId();
    }

    @PostMapping("/detail/payment/cancel/{detailId}")
    public String paymentCancel(
            @RequestParam("templateId") UUID templateId,
            @PathVariable("detailId") UUID detailId) {
        try {
            detailService.expenseDetailCancelPayment(detailId);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/card/" + templateId;
    }

}
