package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailCreateDTOv1;
import org.aturkov.expense.dto.detail.ExpenseDetailCreateRqDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseDetailCreateDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailCreateDTOv1, ExpenseDetailEntity> {

    private final ExpenseDetailSaveDTOReverseMapper expenseDetailSaveDTOReverseMapper;

    @Override
    public void map(ExpenseDetailCreateDTOv1 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        expenseDetailSaveDTOReverseMapper.map(src, dst, mapperContext);
    }
}
