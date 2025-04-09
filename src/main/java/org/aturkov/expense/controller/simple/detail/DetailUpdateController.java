package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailViewRsDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailUpdateRqDTOv1;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.mapper.detail.ExpenseDetailDTOMapperV2;
import org.aturkov.expense.mapper.detail.ExpenseDetailUpdateDTOReverseMapper;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DetailUpdateController {
    private final DetailService detailService;
    private final ExpenseDetailDTOMapperV2 expenseDetailDTOMapperV2;
    private final ExpenseDetailUpdateDTOReverseMapper expenseDetailUpdateDTOReverseMapper;

    @GetMapping("/detail/{id}/update/v1")
    public String showUpdateForm(
            @PathVariable("id") UUID detailId,
            Model model) {
        ExpenseDetailViewRsDTOv1 rs = new ExpenseDetailViewRsDTOv1();
        try {
            ExpenseDetailEntity detail = detailService.findExpenseDetail(detailId);
            rs
                    .setDetail(expenseDetailDTOMapperV2.convert(detail));
            model.addAttribute("rs", rs);
        } catch (ServiceException e) {
            model.addAttribute("errorMessage", "Error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/detail/update-form";
    }

    @PostMapping("/detail/{id}/update/v1")
    public String updateDetailV1(
            @PathVariable("id") UUID detailId,
            ExpenseDetailUpdateRqDTOv1 request) {
        try {
            ExpenseDetailEntity detail = expenseDetailUpdateDTOReverseMapper.convert(request);
            detailService.updateExpenseDetail(detail.setId(detailId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/" + request.getDetail().getTemplateId() + "/card" ;
    }
}
