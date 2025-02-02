package org.aturkov.expense.mapper.deposit;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dto.deposit.DepositUpdateRqDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositUpdateRqDTOMapper extends SimpleDTOMapper<DepositEntity, DepositUpdateRqDTOv1> {

    private final DepositSaveDTOMapper depositSaveDTOMapper;

    @Override
    public void map(DepositEntity src, DepositUpdateRqDTOv1 dst, MapperContext mapperContext) throws Exception {
        depositSaveDTOMapper.map(src, dst, mapperContext);
        dst
                .setId(src.getId());
    }
}
