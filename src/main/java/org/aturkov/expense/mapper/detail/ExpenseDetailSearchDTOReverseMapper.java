package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.domain.DataRange;
import org.aturkov.expense.domain.ExpenseDetailSearch;
import org.aturkov.expense.dto.detail.ExpenseDetailSearchRqDTOv1;
import org.aturkov.expense.mapper.AmountRangeDTOReverseMapper;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.service.other.DateService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.aturkov.expense.service.other.DateService.convertOrNull;

@Component
@RequiredArgsConstructor
public class ExpenseDetailSearchDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailSearchRqDTOv1, ExpenseDetailSearch> {

    private final AmountRangeDTOReverseMapper amountRangeDTOReverseMapper;

    @Override
    public void map(ExpenseDetailSearchRqDTOv1 src, ExpenseDetailSearch dst, MapperContext mapperContext) throws Exception {
        dst
                .setIdList(src.getIdList())
                .setIdExcludeList(src.getIdExcludeList())
                .setTemplateIdList(src.getTemplateIdList())
                .setTemplateIdExcludeList(src.getTemplateIdExcludeList())
                .setNameLikeList(src.getNameLikeList())
                .setNameNotLikeList(src.getNameNotLikeList())
                .setCurrencyTypeList(src.getCurrencyTypeList())
                .setCurrencyTypeExcludeList(src.getCurrencyTypeExcludeList())
                .setPeriodList(src.getPeriodList())
                .setPeriodExcludeList(src.getPeriodExcludeList())
//                .setAmountRange(amountRangeDTOReverseMapper.convert(src.getAmountRange(), mapperContext))
                .setPlanData(convertDataRange(src.getPlanDateFrom(), src.getPlanDateTo()))
//                .setPaid(src.getPaid())
        ;
    }

    private DataRange convertDataRange( LocalDate from, LocalDate to) {
        DataRange dataRange = new DataRange();
        if (from != null)
            dataRange.setFrom(DateService.convertOrNull(from));
        if (to != null)
            dataRange.setTo(DateService.convertOrNull(to));
        return dataRange;
    }
}
