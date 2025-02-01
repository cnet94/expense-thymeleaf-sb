package org.aturkov.expense.service;

import org.aturkov.expense.dao.expenseDetail.ExpenseDetailEntity;
import org.aturkov.expense.dao.historyTransaction.HistoryTransactionEntity;
import org.aturkov.expense.dao.historyTransaction.HistoryTransactionRepository;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    private final HistoryTransactionRepository historyTransactionRepository;

    public HistoryService(HistoryTransactionRepository historyTransactionRepository) {
        this.historyTransactionRepository = historyTransactionRepository;
    }

    public void createHistoryTransaction(ExpenseDetailEntity detail, HistoryTransactionEntity.OperationStatus status) {
        HistoryTransactionEntity transaction = new HistoryTransactionEntity()
                .setOperationType(detail.getTemplate().getOperationType())
                .setExpenseDetailId(detail.getId())
                .setAmount(detail.getAmount())
                .setStatus(status)
                .setCurrency(detail.getCurrency());
        if (detail.getTemplate().getOperationType().equals(TemplateEntity.OperationType.INCOME))
            transaction.setDstDepositId(detail.getTemplate().getDeposit().getId());
        else if (detail.getTemplate().getOperationType().equals(TemplateEntity.OperationType.EXPENSE))
            transaction.setSrcDepositId(detail.getTemplate().getDeposit().getId());
//        else
//            //todo transfer type
        historyTransactionRepository.save(transaction);
    }
}
