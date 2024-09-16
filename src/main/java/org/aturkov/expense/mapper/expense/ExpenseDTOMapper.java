package org.aturkov.expense.mapper.expense;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.ExpenseDetailEntity;
import org.aturkov.expense.dao.ExpenseEntity;
import org.aturkov.expense.dto.ExpenseDTOv1;
import org.aturkov.expense.dto.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.BalanceDTOMapper;
import org.aturkov.expense.mapper.SimpleMapper;
import org.aturkov.expense.service.ExpenseService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseDTOMapper implements SimpleMapper<ExpenseEntity, ExpenseDTOv1> {

    private final ExpenseDetailDTOMapper expenseDetailDTOMapper;
    private final BalanceDTOMapper balanceDTOMapper;
    private final ExpenseService expenseService;

    @Override
    public ExpenseDTOv1 map(ExpenseEntity entity) {
        List<ExpenseDetailDTOv1> expenseDetailList = new ArrayList<>();
        for (ExpenseDetailEntity expenseDetailEntity : entity.getDetails()) {
            expenseDetailList.add(expenseDetailDTOMapper.map(expenseDetailEntity));
        }
        expenseService.fillingExpenseBalance(entity);
        ExpenseDTOv1 expenseDTOv1 = new ExpenseDTOv1();
        expenseDTOv1
                .setId(entity.getId())
                .setName(entity.getName())
                .setType(entity.getType().getAlias())
                .setPaymentDay(entity.getPaymentDay())
                .setPeriod(entity.getPeriod())
                .setExpiryDate(entity.getExpiryDate() != null ? entity.getExpiryDate().toLocalDateTime() : null)
                .setCreateAt(entity.getCreateAt().toLocalDateTime())
                .setDetails(expenseDetailList)
                .setGeneralBalance(balanceDTOMapper.map(entity.getGeneralBalance()));
        expenseDTOv1.getDetails().forEach(e -> e.setName(entity.getName()));
        //todo impl convert to naive currency
//        if (entity.getCurrency() != CURRENCY) {
//            expenseDTOv1
//                    .setTotalAmountCountry(convertCurrency(entity.getTotalAmount()))
//                    .setBalanceOfPaymentCountry(convertCurrency(entity.getBalanceOfPayment()))
//                    .setCurrencyCountry(CURRENCY);
//        }
        return expenseDTOv1;
    }
}
