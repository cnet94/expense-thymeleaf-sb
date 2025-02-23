package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailSaveDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseDetailSaveDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailSaveDTOv1, ExpenseDetailEntity> {

    private final ExpenseDetailDTOReverseMapper expenseDetailDTOReverseMapper;

    @Override
    public void map(ExpenseDetailSaveDTOv1 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        expenseDetailDTOReverseMapper.map(src, dst, mapperContext);
    }
}
