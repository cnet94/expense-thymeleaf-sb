package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dto.detail.*;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.mapper.deposit.DepositDTOMapper;
import org.aturkov.expense.mapper.detail.*;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.service.deposit.DepositService;
import org.aturkov.expense.service.detail.DetailService;
import org.aturkov.expense.service.item.ItemService;
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
    private final ExpenseDetailDTOMapperV2 expenseDetailDTOMapperV2;
    private final ExpenseDetailUpdateDTOReverseMapper expenseDetailUpdateDTOReverseMapper;
    private final ExpenseDetailCreateDTOReverseMapper expenseDetailCreateDTOReverseMapper;
    private final ItemDTOMapper itemDTOMapper;
    private final ItemService itemService;
    private final DepositDTOMapper depositDTOMapper;
    private final DepositService depositService;

    @GetMapping("/detail/list")
    public String showDetails(
            Model model) {
        ExpenseDetailListRsDTOv1 rs = new ExpenseDetailListRsDTOv1();
        try {
            List<ExpenseDetailEntity> list = detailService.getExpenseDetailsForCurrentMonth();
            rs
                    .setDetails(expenseDetailDTOMapperV2.convertCollection(list));
            model.addAttribute("expenseDetails", rs);
            model.addAttribute("deposits", depositDTOMapper.convertCollection(depositService.findDeposits()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/detail/list";
    }

    @GetMapping("/detail/create-form")
    public String showAddDetailForm(
            Model model) {
        ExpenseDetailCreateRqDTOv1 rs = new ExpenseDetailCreateRqDTOv1();
        try {
            model.addAttribute("expenseDetail", rs.setDetail(new ExpenseDetailSaveDTOv1()));
            model.addAttribute("items", itemDTOMapper.convertCollection(itemService.findItems()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/detail/create-form";
    }

    @GetMapping("/detail/add/template/{templateId}/form")
    public String showAddExpenseDetailFormForTemplate(
            @PathVariable("templateId") UUID templateId,
            @RequestParam("name") String name,
            Model model) {
        model.addAttribute("expenseDetail",
                new ExpenseDetailDTOv1()
                        .setTemplateId(templateId)
                        .setName(name));
        return "/detail/card";
    }

    @PostMapping("/detail/save")
    public String saveDetail(
            ExpenseDetailCreateRqDTOv1 request) {
        try {
            ExpenseDetailEntity detail = expenseDetailCreateDTOReverseMapper.convert(request);
            detailService.createExpenseDetail(detail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/detail/list";
//        return "redirect:/template/card/" + request.getDetail().getTemplateId();
    }

    @GetMapping("/detail/update/{detailId}/v1")
    public String showUpdateForm(
            @PathVariable("detailId") UUID detailId,
            Model model) {
        ExpenseDetailSaveRsDTOv1 rs = new ExpenseDetailSaveRsDTOv1();
        try {
            rs
                    .setDetail(expenseDetailDTOMapperV2.convert(detailService.findExpenseDetail(detailId)));
            model.addAttribute("rs", rs);
        } catch (ServiceException e) {
            model.addAttribute("errorMessage", "Error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/detail/update-form";
    }

    @PostMapping("/detail/update/{detailId}")
    public String updateDetailV1(
            @PathVariable("detailId") UUID detailId,
            ExpenseDetailUpdateRqDTOv1 request) {
        try {
            ExpenseDetailEntity detail = expenseDetailUpdateDTOReverseMapper.convert(request);
            detailService.updateExpenseDetail(detail.setId(detailId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/card/" + request.getDetail().getTemplateId();
    }

    @PostMapping("/detail/delete/{detailId}")
    public String deleteDetailV1(
            @PathVariable("detailId") UUID detailId) {
        detailService.deleteExpenseDetail(detailId);
        return "redirect:/detail/list";
    }

    @PostMapping("/detail/payment/approve/{detailId}")
    public String detailPaymentApproveV1(
            @PathVariable("detailId") UUID detailId,
            @RequestParam("depositId") UUID depositId,
            @RequestParam("image") MultipartFile file) {
        ExpenseDetailSaveRsDTOv1 rs = new ExpenseDetailSaveRsDTOv1();
        try {
            ExpenseDetailEntity detail = detailService.approvePaymentDetail(detailId, depositId, file);
            rs
                    .setDetail(expenseDetailDTOMapperV2.convert(detail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        if (rs.getTemplateId() == null)
        return "redirect:/detail/list";
//        else
//            return "redirect:/template/card/" + detail.getTemplateId();
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
