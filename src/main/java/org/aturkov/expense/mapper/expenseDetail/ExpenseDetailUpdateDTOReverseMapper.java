package org.aturkov.expense.mapper.expenseDetail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.expenseDetail.ExpenseDetailEntity;
import org.aturkov.expense.dto.expenseDetail.ExpenseDetailSaveRqDTOv1;
import org.aturkov.expense.dto.expenseDetail.ExpenseDetailUpdateRqDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseDetailUpdateDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailUpdateRqDTOv1, ExpenseDetailEntity> {

    private final ExpenseDetailDTOReverseMapper expenseDetailDTOReverseMapper;

    @Override
    public void map(ExpenseDetailUpdateRqDTOv1 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        expenseDetailDTOReverseMapper.map(src, dst, mapperContext);
    }
}
