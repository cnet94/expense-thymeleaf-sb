package org.aturkov.expense.mapper.expenseDetail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.expenseDetail.ExpenseDetailEntity;
import org.aturkov.expense.dto.expenseDetail.ExpenseDetailDTOv1;
import org.aturkov.expense.dto.expenseDetail.ExpenseDetailSaveRqDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class ExpenseDetailSaveDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailSaveRqDTOv1, ExpenseDetailEntity> {

    private final ExpenseDetailDTOReverseMapper expenseDetailDTOReverseMapper;

    @Override
    public void map(ExpenseDetailSaveRqDTOv1 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        expenseDetailDTOReverseMapper.map(src, dst, mapperContext);
    }
}
