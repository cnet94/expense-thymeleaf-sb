package org.aturkov.expense.mapper.expense;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.ExpenseDetailEntity;
import org.aturkov.expense.dto.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.SimpleMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOReverseMapper implements SimpleMapper<ExpenseDetailDTOv1, ExpenseDetailEntity> {


    @Override
    public ExpenseDetailEntity map(ExpenseDetailDTOv1 entity) {
        ExpenseDetailEntity expenseDetailEntity = new ExpenseDetailEntity()
                .setId(entity.getId())
                .setExpenseId(entity.getExpenseId())
                .setName(entity.getName())
                .setAmount(entity.getAmount())
                .setCurrency(entity.getCurrency())
                .setPeriod(entity.getPeriod())
                .setPlanPaymentDate(Timestamp.valueOf(entity.getPlanPaymentDate()))
                .setPaid(entity.isPaid());
        if (entity.getFactPaymentDate() != null)
            expenseDetailEntity.setFactPaymentDate(Timestamp.valueOf(entity.getFactPaymentDate()));
        return expenseDetailEntity;
    }
}
