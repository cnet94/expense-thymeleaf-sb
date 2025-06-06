package org.aturkov.expense.mapper.deposit;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dto.deposit.DepositSaveDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositSaveDTOMapper extends SimpleDTOMapper<DepositEntity, DepositSaveDTOv1> {
    @Override
    public void map(DepositEntity src, DepositSaveDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst

                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrency(src.getCurrencyType())
                .setStatus(src.getStatus());
    }
}
