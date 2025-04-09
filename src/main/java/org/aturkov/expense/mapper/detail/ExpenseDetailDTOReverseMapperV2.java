package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailDTOv2;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

import static org.aturkov.expense.service.other.DateService.convertOrNull;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOReverseMapperV2 extends SimpleDTOMapper<ExpenseDetailDTOv2, ExpenseDetailEntity> {

    private final ExpenseDetailDTOReverseMapper expenseDetailDTOReverseMapper;

    @Override
    public void map(ExpenseDetailDTOv2 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        expenseDetailDTOReverseMapper.map(src, dst, mapperContext);


    }
}
