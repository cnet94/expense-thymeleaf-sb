package org.aturkov.expense.mapper.expense;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.ExpenseDetailEntity;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.dto.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.SimpleMapper;
import org.aturkov.expense.service.ExpenseDetailService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOMapper implements SimpleMapper<ExpenseDetailEntity, ExpenseDetailDTOv1> {

    private final ExpenseDetailService expenseDetailService;

    @Override
    public ExpenseDetailDTOv1 map(ExpenseDetailEntity entity) {
        ExpenseDetailDTOv1 expenseDetailDTOv1 = new ExpenseDetailDTOv1();
        expenseDetailDTOv1
                .setId(entity.getId())
                .setExpenseId(entity.getExpenseId())
                .setName(entity.getName())
                .setAmount(entity.getAmount())
                .setCurrency(entity.getCurrency())
                .setPeriod(entity.getPeriod())
                .setPlanPaymentDate(entity.getPlanPaymentDate().toLocalDateTime())
                .setPaid(entity.isPaid());
        if (entity.getFactPaymentDate() != null) {
            ValidityPeriod validityPeriod = expenseDetailService.calculatePeriod(entity.getFactPaymentDate().toLocalDateTime(), ValidityPeriod.Time.currentOffset(entity.getPeriod()));
            expenseDetailDTOv1
                    .setFactPaymentDate(entity.getFactPaymentDate().toLocalDateTime())
                    .setPaymentPeriodFrom(validityPeriod.getFrom())
                    .setPaymentPeriodTo(validityPeriod.getTo());
        }
        //todo ApiUser
//        if (entity.getCurrency() != CURRENCY) {
//            expenseDetailDTOv1
//                    .set(convertCurrency(entity.getAmount()))
//                    .setCurrencyCountry(CURRENCY);
//        }
        return expenseDetailDTOv1;
    }
}
