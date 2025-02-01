package org.aturkov.expense.controller.expenseDetail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.expenseDetail.ExpenseDetailEntity;
import org.aturkov.expense.dto.expenseDetail.ExpenseDetailUpdateRqDTOv1;
import org.aturkov.expense.mapper.expenseDetail.ExpenseDetailDTOMapper;
import org.aturkov.expense.mapper.expenseDetail.ExpenseDetailSaveDTOReverseMapper;
import org.aturkov.expense.mapper.expenseDetail.ExpenseDetailUpdateDTOReverseMapper;
import org.aturkov.expense.service.DetailService;
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
