package org.aturkov.expense.mapper.other;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.domain.Balance;
import org.aturkov.expense.dto.BalanceDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.service.TemplateService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceDTOMapper extends SimpleDTOMapper<Balance, BalanceDTOv1> {
    final TemplateService templateService;

    @Override
    public void map(Balance src, BalanceDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setTotalAmount(src.getTotalAmount())
                .setRemainderAmount(src.getRemainderAmount())
                .setCurrency(src.getCurrency());
    }
}
