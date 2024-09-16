package org.aturkov.expense.service;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.*;
import org.aturkov.expense.domain.Balance;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static org.aturkov.expense.mapper.FormatMapper.doubleToString;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseDetailRepository expenseDetailRepository;

    public List<ExpenseEntity> getAll() {
        List<ExpenseEntity> all = expenseRepository.findAll();
        if (all.isEmpty())
            return null;
        for (ExpenseEntity expenseEntity : all) {
            fillingExpenseBalance(expenseEntity);
        }
        return all;
    }

    public ExpenseEntity getExpenseById(UUID id) {
        ExpenseEntity expenseEntity = expenseRepository.findById(id).get();
        if (expenseEntity != null) {
            fillingExpenseBalance(expenseEntity);
            if (expenseEntity.getDetails() != null) {
                expenseEntity.getDetails().sort(Comparator.comparing(ExpenseDetailEntity::getPlanPaymentDate));
            }
            return expenseEntity;
        }
        throw new RuntimeException();
    }

    public void fillingExpenseBalance(ExpenseEntity expenseEntity) {
        Balance balance = new Balance()
                .setTotalAmount(countTotal(expenseEntity.getDetails()))
                .setRemainderAmount(countBalanceOfPayment(expenseEntity.getDetails()))
                .setCurrency(expenseEntity.getCurrency());
        expenseEntity.setGeneralBalance(balance);
    }

    public boolean checkExistsType(ExpenseEntity expense, List<ExpenseEntity.Type> types) {
        return types.stream().anyMatch(t -> t.equals(expense.getType()));
    }

    public boolean checkExpiryDate(ExpenseEntity expense) {
        if (expense.getExpiryDate() == null)
            return true;
        LocalDate expiryDate = expense.getExpiryDate().toLocalDateTime().toLocalDate();
        LocalDate nextPaymentDate = LocalDate.now().plusMonths(1).withDayOfMonth(expense.getPaymentDay());
        return nextPaymentDate.isBefore(expiryDate);
    }

    private String countTotal(List<ExpenseDetailEntity> list) {
        return doubleToString(list.stream().mapToDouble(ExpenseDetailEntity::getAmount).sum());
    }

    private String countBalanceOfPayment(List<ExpenseDetailEntity> list) {
        return doubleToString(list.stream().filter(e -> !e.isPaid()).mapToDouble(ExpenseDetailEntity::getAmount).sum());
    }
}
