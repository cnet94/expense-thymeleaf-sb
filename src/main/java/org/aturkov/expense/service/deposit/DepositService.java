package org.aturkov.expense.service.deposit;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.dao.deposit.DepositRepository;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.service.EntitySecureFindServiceImpl;
import org.aturkov.expense.service.EntitySmartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositService extends EntitySecureFindServiceImpl<DepositEntity> {
    private final DepositRepository depositRepository;

    @Override
    public boolean validateEntity(DepositEntity entity, EntitySmartService.EntityValidateMode entityValidateMode) throws ServiceException {
        return true;
    }

    public DepositEntity findDeposit(UUID id) throws ServiceException {
        return safeFindEntity(id, depositRepository, FindMode.ifNullThrowsError);
    }

    public List<DepositEntity> findDeposits() {
        return depositRepository.findAll();
    }

    public void changeDepositAmount(ExpenseDetailEntity detail, final boolean adding) throws ServiceException {
        DepositEntity deposit;
        if (detail.getTemplate() == null) {
            deposit = safeFindEntity(detail.getDepositId(), depositRepository, FindMode.ifNullThrowsError);
        } else {
            deposit = detail.getTemplate().getDeposit();
        }
        //todo сизменить логику, а то как то криво вышло
        if (adding) {
            switch (detail.getOperationType()) {
                case INCOME -> deposit.addAmount(detail.getAmount());
                case EXPENSE -> deposit.subtractAmount(detail.getAmount());
            };
        } else {
            switch (detail.getOperationType()) {
                case INCOME -> deposit.subtractAmount(detail.getAmount());
                case EXPENSE -> deposit.addAmount(detail.getAmount());
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

    public DepositEntity createDeposit(DepositEntity deposit) throws ServiceException {
        validateEntity(deposit, EntitySmartService.EntityValidateMode.beforeSave);
        deposit
                .setName(deposit.getName() + " (" +  deposit.getCurrencyType().name() + ")")
                .setAmount(0.0)
                .setStatus(DepositEntity.Status.active);
        return depositRepository.save(deposit);
    }

    public DepositEntity updateDeposit(DepositEntity deposit) {
        return depositRepository.save(deposit);
    }

    public void deleteDeposit(UUID depositId) {
        depositRepository.deleteById(depositId);
    }

    public void changeStatus(UUID depositId) throws ServiceException {
        DepositEntity dbDeposit = safeFindEntity(depositId, depositRepository, FindMode.ifNullThrowsError);
        DepositEntity.Status status = dbDeposit.getStatus();
        if (status.equals(DepositEntity.Status.active)) {
            status = DepositEntity.Status.archived;
        } else {
            status = DepositEntity.Status.active;
        }
        dbDeposit.setStatus(status);
        depositRepository.save(dbDeposit);
    }
}
