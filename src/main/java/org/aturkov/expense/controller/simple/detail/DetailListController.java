package org.aturkov.expense.controller.simple.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.cambium.pagination.PaginationResult;
import org.aturkov.cambium.pagination.SimplePagination;
import org.aturkov.expense.controller.rest.annotation.SimplePaginationParams;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.ExpenseDetailSearch;
import org.aturkov.expense.dto.detail.ExpenseDetailSearchRqDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailSearchRsDTOv1;
import org.aturkov.expense.mapper.deposit.DepositDTOMapper;
import org.aturkov.expense.mapper.detail.ExpenseDetailDTOMapperV2;
import org.aturkov.expense.mapper.detail.ExpenseDetailSearchDTOReverseMapper;
import org.aturkov.expense.mapper.pagination.PaginationMapper;
import org.aturkov.expense.service.deposit.DepositService;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class DetailListController {
    private final DetailService detailService;
    private final ExpenseDetailDTOMapperV2 expenseDetailDTOMapperV2;
    private final DepositDTOMapper depositDTOMapper;
    private final DepositService depositService;
    private final ExpenseDetailSearchDTOReverseMapper expenseDetailSearchDTOReverseMapper;
    private final PaginationMapper paginationMapper;

    @GetMapping("/detail/list")
    public String showDetails(
            @SimplePaginationParams(sortField = ExpenseDetailEntity.Fields.planPaymentDate) SimplePagination pagination,
            ExpenseDetailSearchRqDTOv1 request,
            Model model) {
        ExpenseDetailSearchRsDTOv1 rs = new ExpenseDetailSearchRsDTOv1();
        try {
            ExpenseDetailSearch convert = expenseDetailSearchDTOReverseMapper.convert(request);
            PaginationResult<ExpenseDetailEntity> details = detailService.findDetails(convert, pagination);
            rs
                    .setDetails(expenseDetailDTOMapperV2.convertCollection(details.getList()))
                    .setPagination(paginationMapper.convert(details));
            model.addAttribute("rs", rs);
            model.addAttribute("deposits", depositDTOMapper.convertCollection(depositService.findDeposits()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "/detail/list";
    }
}
