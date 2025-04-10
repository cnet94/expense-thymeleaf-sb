package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailViewRsDTOv1;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.mapper.detail.ExpenseDetailDTOMapperV2;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DetailController {
    private final DetailService detailService;
    private final ExpenseDetailDTOMapperV2 expenseDetailDTOMapperV2;

    @PostMapping("/detail/{id}/payment/approve/v1")
    public String detailPaymentApproveV1(
            @PathVariable("id") UUID detailId,
            @RequestParam("depositId") UUID depositId,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        ExpenseDetailViewRsDTOv1 rs = new ExpenseDetailViewRsDTOv1();
        ExpenseDetailEntity detail;
        try {
            detail = detailService.approvePaymentDetail(detailId, depositId, file);
            rs
                    .setDetail(expenseDetailDTOMapperV2.convert(detail));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
            return "redirect:/detail/list";
        }
        return "redirect:/detail/list";
    }

    @PostMapping("/detail/{id}/payment/approve/v2")
    public String detailPaymentApproveV2(
            @PathVariable("id") UUID detailId,
            @RequestParam("templateId") UUID templateId,
            @RequestParam("depositId") UUID depositId,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        ExpenseDetailViewRsDTOv1 rs = new ExpenseDetailViewRsDTOv1();
        ExpenseDetailEntity detail;
        try {
            detail = detailService.approvePaymentDetail(detailId, depositId, file);
            rs
                    .setDetail(expenseDetailDTOMapperV2.convert(detail));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving template: " + e.getMessage());
            return "redirect:/template/" + templateId + "/card";
        }
        return "redirect:/template/" + templateId + "/card";
    }

    @PostMapping("/detail/{detailId}/payment/cancel/v1")
    public String paymentCancelV1(
            @PathVariable("detailId") UUID detailId) {
        try {
            detailService.expenseDetailCancelPayment(detailId);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/detail/list";
    }

    @PostMapping("/detail/{detailId}/payment/cancel/v2")
    public String paymentCancelV2(
            @RequestParam("templateId") UUID templateId,
            @PathVariable("detailId") UUID detailId) {
        try {
            detailService.expenseDetailCancelPayment(detailId);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/" + templateId + "/card";
    }
}
