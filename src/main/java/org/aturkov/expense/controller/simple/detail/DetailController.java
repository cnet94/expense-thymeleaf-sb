package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.cambium.pagination.PaginationResult;
import org.aturkov.cambium.pagination.SimplePagination;
import org.aturkov.expense.controller.rest.annotation.SimplePaginationParams;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.ExpenseDetailSearch;
import org.aturkov.expense.dto.detail.ExpenseDetailViewRsDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailSearchRqDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailSearchRsDTOv1;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.mapper.detail.ExpenseDetailDTOMapperV2;
import org.aturkov.expense.mapper.detail.ExpenseDetailSearchDTOReverseMapper;
import org.aturkov.expense.mapper.pagination.PaginationMapper;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DetailController {
    private final DetailService detailService;
    private final PaginationMapper paginationMapper;
    private final ExpenseDetailDTOMapperV2 expenseDetailDTOMapperV2;
    private final ExpenseDetailSearchDTOReverseMapper expenseDetailSearchDTOReverseMapper;

    @PostMapping("/detail/{detailId}/payment/approve")
    public String detailPaymentApproveV1(
            @PathVariable("detailId") UUID detailId,
            @RequestParam("depositId") UUID depositId,
            @RequestParam("image") MultipartFile file) {
        ExpenseDetailViewRsDTOv1 rs = new ExpenseDetailViewRsDTOv1();
        ExpenseDetailEntity detail;
        try {
            detail = detailService.approvePaymentDetail(detailId, depositId, file);
            rs
                    .setDetail(expenseDetailDTOMapperV2.convert(detail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (rs.getDetail().getTemplateId() == null)
            return "redirect:/detail/list";
        else
            return "redirect:/template/" + detail.getTemplateId() + "/card";
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
