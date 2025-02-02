package org.aturkov.expense.mapper.deposit;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dto.deposit.DepositSaveDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositSaveDTOReverseMapper extends SimpleDTOMapper<DepositSaveDTOv1, DepositEntity> {
    @Override
    public void map(DepositSaveDTOv1 src, DepositEntity dst, MapperContext mapperContext) throws Exception {
        dst
                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrencyType(src.getCurrencyType())
                .setStatus(src.getStatus());
    }
}
