package org.aturkov.expense.mapper.deposit;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dto.deposit.DepositDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositDTOMapper extends SimpleDTOMapper<DepositEntity, DepositDTOv1> {

    @Override
    public void map(DepositEntity src, DepositDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrency(src.getCurrency());
    }
}
