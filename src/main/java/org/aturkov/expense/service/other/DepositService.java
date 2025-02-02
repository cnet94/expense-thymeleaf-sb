package org.aturkov.expense.service.other;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dao.deposit.DepositRepository;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;

    public List<DepositEntity> getDeposits() {
        return depositRepository.findAll();
    }

    public void changeDepositAmount(ExpenseDetailEntity detail, final boolean adding) {
        //todo сизменить логику, а то как то криво вышло
        DepositEntity deposit = detail.getTemplate().getDeposit();
        if (adding) {
            switch (detail.getTemplate().getOperationType()) {
                case INCOME -> deposit.addAmount(detail.getAmount());
                case EXPENSE -> deposit.subtractAmount(detail.getAmount());
//            case TRANSFER -> ;
            };
        } else {
            switch (detail.getTemplate().getOperationType()) {
                case INCOME -> deposit.subtractAmount(detail.getAmount());
                case EXPENSE -> deposit.addAmount(detail.getAmount());
//            case TRANSFER -> ;
            };
        }
        depositRepository.save(deposit);
    }

    public void revertAmountDeposit(ExpenseDetailEntity dbExpenseDetail) {
//        double amount = dbExpenseDetail.getAmount();
//        DepositEntity deposit = dbExpenseDetail.getDeposit();
//        deposit.setAmount(deposit.getAmount() - amount);
//        depositRepository.save(deposit);
    }
}
