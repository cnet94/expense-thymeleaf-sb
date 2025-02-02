package org.aturkov.expense.mapper.deposit;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dto.deposit.DepositUpdateRqDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositUpdateRqDTOReverseMapper extends SimpleDTOMapper<DepositUpdateRqDTOv1, DepositEntity> {

    private final DepositSaveDTOReverseMapper depositSaveDTOReverseMapper;

    @Override
    public void map(DepositUpdateRqDTOv1 src, DepositEntity dst, MapperContext mapperContext) throws Exception {
        depositSaveDTOReverseMapper.map(src, dst, mapperContext);
    }
}
