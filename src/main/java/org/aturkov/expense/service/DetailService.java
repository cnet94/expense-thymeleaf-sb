package org.aturkov.expense.service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.aturkov.expense.ServiceException;
import org.aturkov.expense.dao.historyTransaction.HistoryTransactionEntity;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dao.expenseDetail.ExpenseDetailEntity;
import org.aturkov.expense.dao.expenseDetail.ExpenseDetailRepository;
import org.aturkov.expense.domain.ValidityPeriod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import static org.aturkov.expense.service.DateService.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailService extends EntitySecureFindServiceImpl<ExpenseDetailEntity> {
    private final DateService dateService;
    private final TemplateService templateService;
    private final ExpenseDetailRepository expenseDetailRepository;
    private final AttachmentService attachmentService;
    private final DepositService depositService;
    private final HistoryService historyService;

    @Override
    public boolean validateEntity(ExpenseDetailEntity entity, EntitySmartService.EntityValidateMode entityValidateMode) throws ServiceException {
        return true;
    }

    public ExpenseDetailEntity findExpenseDetail(UUID id) throws ServiceException {
        return safeFindEntity(id, expenseDetailRepository, FindMode.ifNullThrowsError);
    }

    public void saveExpenseDetail(List<ExpenseDetailEntity> expenseDetailList) {
        expenseDetailRepository.saveAll(expenseDetailList);
    }

    public void createDetail(TemplateEntity template) throws ServiceException {
        if (template.getOperationType().equals(TemplateEntity.OperationType.INCOME))
            createIncomeDetail(template);
        else
            createExpenseDetail(template);
    }

    public void createExpenseDetail(ExpenseDetailEntity expenseDetail) throws ServiceException {
        TemplateEntity dbTemplate = templateService.safeFindTemplate(expenseDetail.getTemplateId(), FindMode.ifNullThrowsError);
        expenseDetail
                .setOrder(nextOrderForDetailExpense(dbTemplate))
                .setPeriod(dbTemplate.getPeriod());
        expenseDetailRepository.save(expenseDetail);
    }

    public void createExpenseDetail(TemplateEntity template) throws ServiceException {
        ExpenseDetailEntity detail = fillingGeneralDetail(template);
//        validationDetail(template);
        // не создаем detail, потому что не позможно сейчас создать detail с ткущим template
        if (!fillingAmountAndCurrency(template, detail))
            return;

        switch (template.getType()) {
            case BASIC -> fillingExpenseWithTypeOneTime(template, detail);
            case RECURRING -> fillingExpenseWithTypeRecurring(template, detail);
            case FIXED -> fillingExpenseWithTypeCredit(template, detail);
        }
    }

    public void createIncomeDetail(TemplateEntity template) throws ServiceException {
        //todo написать метод, который будет проверять что в шаблоне который установлен что там есть сумма за тот период ха который я должен создать детаил
//        dateService.checkIncomeLastMonthAlreadyFinished(template);
        ExpenseDetailEntity incomeDetail = fillingGeneralIncomeDetail(template);
        incomeDetail
                .setAmount(template.getAmount())
                .setPlanPaymentDate(Timestamp.valueOf(dateService.nextPaymentDate(template).atStartOfDay()))
                .setOrder(nextOrderForDetailExpense(template));
        expenseDetailRepository.save(incomeDetail);
    }

    public ExpenseDetailEntity updateExpenseDetail(ExpenseDetailEntity expenseDetailEntity) throws ServiceException {
        TemplateEntity dbExpense = templateService.safeFindTemplate(expenseDetailEntity.getTemplateId(), FindMode.ifNullThrowsError);
        ExpenseDetailEntity dbExpenseDetail = dbExpense.getDetails().stream()
                .filter(e -> e.getId().equals(expenseDetailEntity.getId()))
                .findFirst().get();
        if (dbExpenseDetail.isPaid()) {
            dbExpenseDetail.setName(expenseDetailEntity.getName());
        } else {
            dbExpenseDetail
                    .setName(expenseDetailEntity.getName())
                    .setAmount(expenseDetailEntity.getAmount())
                    .setCurrency(expenseDetailEntity.getCurrency())
                    .setPlanPaymentDate(expenseDetailEntity.getPlanPaymentDate());
        }
        return expenseDetailRepository.save(dbExpenseDetail);
    }

    @Transactional
    public void deleteExpenseDetail(UUID detailId) {
        deleteExpenseDetail(Collections.singletonList(detailId));
    }

    @Transactional
    public void deleteExpenseDetail(List<UUID> detailIds) {
        if (CollectionUtils.isEmpty(detailIds))
            return;
        expenseDetailRepository.deleteAllByIdIn(detailIds);
        for (UUID detailId : detailIds) {
            log.info("expenseDetail[{}] perhaps was deleted", detailId);
        }
    }

    @Transactional
    public ExpenseDetailEntity approvePaymentDetail(UUID detailId, MultipartFile file) throws IOException, ServiceException {
        ExpenseDetailEntity detail = findExpenseDetail(detailId);

//        boolean checkActualDate = dateService.checkActualPaymentPeriod(foundExpense);
//        if (!checkActualDate)
//            throw new ServiceException("Data is not actual");

        updatingDetailWhenApprovePayment(detail);
        checkCreatingNextDetail(detail.getTemplate());

        depositService.changeDepositAmount(detail, true);
        historyService.createHistoryTransaction(detail, HistoryTransactionEntity.OperationStatus.APPROVE);
        attachmentService.saveAttachment(detailId, file);
        expenseDetailRepository.save(detail);
        return detail;
    }

    private void updatingDetailWhenApprovePayment(ExpenseDetailEntity detail) {
        dateService.fillingPeriod(detail);
        detail
                .setPaid(true)
                .setFactPaymentDate(Timestamp.from(Instant.now()));
    }

    private void checkCreatingNextDetail(TemplateEntity template) throws ServiceException {
        TemplateEntity.OperationType operationType = template.getOperationType();
        TemplateEntity.Type type = template.getType();
        if (!type.equals(TemplateEntity.Type.RECURRING))
            return;
        if (operationType.equals(TemplateEntity.OperationType.EXPENSE))
            createExpenseDetail(template);
        else {
            createIncomeDetail(template);
        }
//        else {
//            if (!hasNotPaid)
//                templateService.updateTemplate(dbTemplate.setActive(false));
//        }
    }

    private ExpenseDetailEntity fillingGeneralIncomeDetail(TemplateEntity template) throws ServiceException {
        return new ExpenseDetailEntity()
                .setTemplateId(template.getId())
                .setPeriod(template.getPeriod())
                .setName(template.getName())
                .setCurrency(template.getCurrency())
                .setPaid(false);
    }

    private void fillingExpenseWithTypeRecurring(TemplateEntity template, ExpenseDetailEntity detail) throws ServiceException {
        detail
                .setOrder(nextOrderForDetailExpense(template))
                .setPlanPaymentDate(convertToTimestamp(dateService.nextPaymentDate(template)));
        expenseDetailRepository.save(detail);
    }

    private boolean fillingAmountAndCurrency(TemplateEntity template, ExpenseDetailEntity detail) throws ServiceException {
        LocalDate dateOfMonth = LocalDate.now().plusMonths(template.getPeriod().getOffset());
        ValidityPeriod period = dateService.getPeriod(dateOfMonth);

        if (!checkDetailHasPaidInPeriod(template.getDependTemplate(), period))
            return false;

        double sum = 0.0;
        for (ExpenseDetailEntity dependDetail : template.getDependTemplate().getDetails()) {
            if (Boolean.TRUE.equals(dependDetail.isPaid())
                    && dependDetail.getFactPaymentDate() != null
                    && dateService.dateIsIncludeInPeriod(dependDetail.getFactPaymentDate(), period))
                        sum += dependDetail.getAmount();
        }

        detail
                .setAmount(calculatePercent(sum, template.getPercent()))
                .setCurrency(template.getCurrency());
        return true;
    }

    private boolean checkDetailHasPaidInPeriod(TemplateEntity dependTemplate, ValidityPeriod period) throws ServiceException {
        boolean exists = false;
        for (ExpenseDetailEntity detail : dependTemplate.getDetails()) {
            if (detail.isPaid()
                    && dateService.dateIsIncludeInPeriod(detail.getPlanPaymentDate(), period)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    private Double calculatePercent(Double amount, Double percent) {
        if (amount == null || percent == null) {
            throw new IllegalArgumentException("Amount and percent must not be null");
        }
        return (amount * percent) / 100;
    }

    private void fillingExpenseWithTypeOneTime(TemplateEntity template, ExpenseDetailEntity detail) {
        detail
                .setOrder(nextOrderForDetailExpense(template))
                .setPlanPaymentDate(template.getTempPaymentDate());
        expenseDetailRepository.save(detail);
    }

    private void fillingExpenseWithTypeCredit(TemplateEntity template, ExpenseDetailEntity detail) throws ServiceException {
        if (template.getPaymentCount() > 1) {
            List<ExpenseDetailEntity> saveDetailList = fillingExpenseDetailList(template);
            saveExpenseDetail(saveDetailList);
        } else {
            detail
                    .setAmount(template.getDetailAmount());
            expenseDetailRepository.save(detail);
        }
    }

    private ExpenseDetailEntity fillingGeneralDetail(TemplateEntity template) throws ServiceException {
        return new ExpenseDetailEntity()
                .setTemplateId(template.getId())
                .setName(template.getName())
                .setCurrency(template.getCurrency())
                .setPeriod(template.getPeriod())
                .setPaid(false);
    }

    private List<ExpenseDetailEntity> fillingExpenseDetailList(TemplateEntity template) throws ServiceException {
        List<ExpenseDetailEntity> list = new ArrayList<>();
        Timestamp nextPaymentDate = template.getPaymentDate();
        AmountPerMonth amountPerMonth = amountPerMonth(template);
        for (int i = 1; i <= template.getPaymentCount(); i++) {
            if (i != 1) {
                nextPaymentDate = convertToTimestamp(dateService.nextPaymentDateForCredit(template, nextPaymentDate));
                if (i == 2)
                    template.setPaymentInCurrentMonth(false);
            }
            ExpenseDetailEntity entity = fillingGeneralDetail(template);
            entity
                    .setOrder(i)
                    .setPlanPaymentDate(nextPaymentDate);
            if (i != template.getPaymentCount())
                entity.setAmount(amountPerMonth.getAvg());
            else
                entity.setAmount(amountPerMonth.getLastAmount());
            list.add(entity);
        }
        return list;
    }

    private AmountPerMonth amountPerMonth(TemplateEntity template) {
        double amount = template.getDetailAmount();
        double lastAmount = template.getReminder();
        int paymentCountExcludeLastMonth = template.getPaymentCount() - 1;
        double avgAmount = roundDouble((amount - lastAmount) / paymentCountExcludeLastMonth);
        return new AmountPerMonth()
                .setLastAmount(lastAmount)
                .setAvg(avgAmount);
    }

    public void expenseDetailCancelPayment(UUID id) throws ServiceException {
        ExpenseDetailEntity dbDetail = expenseDetailRepository.findById(id).orElse(null);
        if (dbDetail == null) {
            throw new ServiceException("Expense detail not found");
        }
        dbDetail
                .setPaid(false)
                .setFactPaymentDate(null);
        depositService.changeDepositAmount(dbDetail, false);
        historyService.createHistoryTransaction(dbDetail, HistoryTransactionEntity.OperationStatus.CANCEL);
        expenseDetailRepository.save(dbDetail);
    }

    public List<ExpenseDetailEntity> getExpenseDetailsForCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        //todo заменить поиском
        List<ExpenseDetailEntity> byPaymentDateInCurrentMonth = expenseDetailRepository.findByPaymentDateAndNotPaidInCurrentMonth(currentMonth, currentYear);
        byPaymentDateInCurrentMonth.sort(Comparator.comparing(ExpenseDetailEntity::getPlanPaymentDate));
        return byPaymentDateInCurrentMonth;
    }

    public List<ExpenseDetailEntity> getIncomeDetails() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        List<ExpenseDetailEntity> byPaymentDateInCurrentMonth = expenseDetailRepository.findByPaymentDateAndNotPaidInCurrentMonth(currentMonth, currentYear);
        byPaymentDateInCurrentMonth.sort(Comparator.comparing(ExpenseDetailEntity::getPlanPaymentDate));
        return byPaymentDateInCurrentMonth;
    }

    private int nextOrderForDetailExpense(TemplateEntity template) {
        if (template.getDetails().isEmpty())
            return 1;
        return template.getDetails().stream().mapToInt(ExpenseDetailEntity::getOrder).max().getAsInt() + 1;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    private class AmountPerMonth {
        double lastAmount;
        double avg;
    }
}
