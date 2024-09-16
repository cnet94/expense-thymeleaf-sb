package org.aturkov.expense.mapper;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.domain.Balance;
import org.aturkov.expense.dto.BalanceDTOv1;
import org.aturkov.expense.service.ExpenseService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceDTOMapper implements SimpleMapper<Balance, BalanceDTOv1> {
    final ExpenseService expenseService;

    @Override
    public BalanceDTOv1 map(Balance entity) {
        return new BalanceDTOv1()
                .setTotalAmount(entity.getTotalAmount())
                .setRemainderAmount(entity.getRemainderAmount())
                .setCurrency(entity.getCurrency());
    }
}
