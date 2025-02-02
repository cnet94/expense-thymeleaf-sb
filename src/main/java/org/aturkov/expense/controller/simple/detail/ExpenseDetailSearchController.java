package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailUpdateRqDTOv1;
import org.aturkov.expense.mapper.detail.ExpenseDetailDTOMapper;
import org.aturkov.expense.mapper.detail.ExpenseDetailSaveDTOReverseMapper;
import org.aturkov.expense.mapper.detail.ExpenseDetailUpdateDTOReverseMapper;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ExpenseDetailSearchController {
    private final DetailService detailService;
    private final ExpenseDetailDTOMapper expenseDetailDTOMapper;
    private final ExpenseDetailSaveDTOReverseMapper expenseDetailSaveDTOReverseMapper;
    private final ExpenseDetailUpdateDTOReverseMapper expenseDetailUpdateDTOReverseMapper;

    @PostMapping("/detail/search/V1")
    public String expenseDetailSearchV1(
            ExpenseDetailUpdateRqDTOv1 request) {
        try {
            ExpenseDetailEntity detail = expenseDetailUpdateDTOReverseMapper.convert(request);
            detailService.updateExpenseDetail(detail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/template/card/" + request.getTemplateId();
    }
}
