package org.aturkov.expense.service.detail;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.aturkov.cambium.pagination.PaginationResult;
import org.aturkov.cambium.pagination.SimplePagination;
import org.aturkov.cambium.pagination.util.PaginationUtils;
import org.aturkov.expense.domain.*;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dao.detail.ExpenseDetailRepository;
import org.aturkov.expense.service.*;
import org.aturkov.expense.service.attachment.AttachmentService;
import org.aturkov.expense.service.history.HistoryService;
import org.aturkov.expense.service.other.DateService;
import org.aturkov.expense.service.deposit.DepositService;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import static org.aturkov.expense.dao.specification.CommonSpecification.*;
import static org.aturkov.expense.service.other.DateService.*;

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

    public PaginationResult<ExpenseDetailEntity> findDetails(ExpenseDetailSearch search, SimplePagination pagination) throws ServiceException {
        Specification<ExpenseDetailEntity> spec = createDetailSearchSpecification(search);
        Page<ExpenseDetailEntity> ret = expenseDetailRepository.findAll(spec, PaginationUtils.pageableOffset(pagination));
        return PaginationUtils.convertInPaginationResult(ret, pagination);
    }

    private Specification<ExpenseDetailEntity> createDetailSearchSpecification(ExpenseDetailSearch search) {
        return Specification.allOf(
                checkUuidIn(search.getIdList(), false, false, ExpenseDetailEntity.Fields.id),
                checkUuidIn(search.getIdExcludeList(), true, false, ExpenseDetailEntity.Fields.id),
                checkUuidIn(search.getTemplateIdList(), false, false, ExpenseDetailEntity.Fields.templateId),
                checkUuidIn(search.getTemplateIdExcludeList(), true, false, ExpenseDetailEntity.Fields.templateId),
                checkFieldLikeIn(search.getNameLikeList(), false, false, ExpenseDetailEntity.Fields.name),
                checkFieldLikeIn(search.getNameNotLikeList(), true, true, ExpenseDetailEntity.Fields.name),
                checkFieldLocalDateTimeBetween(search.getPlanData(), ExpenseDetailEntity.Fields.planPaymentDate)
        );
    }

    public void createSimpleDetail(ExpenseDetailEntity detail) throws ServiceException {
        TemplateEntity dbTemplate = templateService.safeFindTemplate(detail.getTemplateId(), FindMode.ifNullThrowsError);
        if (detail.getTemplateId() != null)
            detail = fillGeneralIncomeDetail(dbTemplate, detail);
        expenseDetailRepository.save(detail);
    }

    public void createDetail(TemplateEntity template) throws ServiceException {
        if (!checkCreateOpportunity(template))
            return;
        if (template.getOperationType().equals(OperationType.INCOME))
            createIncomeDetail(template);
        else
            createExpenseDetail(template);
    }

    public void createExpenseDetail(TemplateEntity template) throws ServiceException {
        ExpenseDetailEntity detail = createGeneralDetail(template);
        fillAmount(template, detail);
        fillCurrency(template, detail);
        fillOrder(template, detail);
        switch (template.getType()) {
            case RECURRING -> fillRecurringDetailAndSave(template, detail);
            case FIXED -> fillingFixedDetailAndSave(template, detail);
        }
    }

    public void createIncomeDetail(TemplateEntity template) throws ServiceException {
        //todo написать метод, который будет проверять что в шаблоне который установлен что там есть сумма за тот период ха который я должен создать детаил
//        dateService.checkIncomeLastMonthAlreadyFinished(template);
        ExpenseDetailEntity incomeDetail = createGeneralIncomeDetail(template);
        incomeDetail
                .setOperationType(template.getOperationType())
                .setAmount(template.getAmount())
                .setPlanPaymentDate(Timestamp.valueOf(dateService.nextPaymentDate(template).atStartOfDay()))
                .setOrder(nextOrderForDetailExpense(template))
                .setItemId(template.getItemId());
        expenseDetailRepository.save(incomeDetail);
    }

    private void fillOrder(TemplateEntity template, ExpenseDetailEntity detail) {
        detail.setOrder(nextOrderForDetailExpense(template));
    }

    private void fillCurrency(TemplateEntity template, ExpenseDetailEntity detail) {
        if (template.getCurrency() != null)
            detail.setCurrency(template.getCurrency());
    }

    public ExpenseDetailEntity updateExpenseDetail(ExpenseDetailEntity expenseDetailEntity) throws ServiceException {
        //todo текущая логика учитывает только если есть template написать если нет
        TemplateEntity dbExpense = templateService.safeFindTemplate(expenseDetailEntity.getTemplateId(), FindMode.ifNullNone);
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
    public void deleteExpenseDetail(UUID detailId, Boolean paid) throws ServiceException {
        if (paid)
            throw new ServiceException("Can't delete expense detail because it is paid");
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
    public ExpenseDetailEntity approvePaymentDetail(UUID detailId, UUID depositId, MultipartFile file) throws IOException, ServiceException {
        ExpenseDetailEntity detail = findExpenseDetail(detailId);

        // написать логику по которой будет проверятся возможно ли оплатить текущий счет исходя из логики
        // если платеж имеет период чтоб этот период не конфликтовал с текущим периодом, например чтоб я не мог оплатить за следующий пеориод, в текущем
        // потому что в шаблонеуказана что оплата за текущий период, а он еще активен
//        boolean checkActualDate = dateService.checkActualPaymentPeriod(foundExpense);
//        if (!checkActualDate)
//            throw new ServiceException("Data is not actual");

        fillDetail(detail, depositId);
        if (detail.getTemplateId() != null)
            createDetail(detail.getTemplate());
//        historyService.createHistoryTransaction(detail, HistoryTransactionEntity.OperationStatus.APPROVE);
        depositService.changeDepositAmount(detail,true);
        attachmentService.saveAttachment(detailId, file);
        expenseDetailRepository.save(detail);
        return detail;
    }

    private void fillDetail(ExpenseDetailEntity detail, UUID depositId) {
        dateService.fillRangePeriod(detail);
        detail
                .setPaid(true)
                .setDepositId(depositId)
                .setFactPaymentDate(Timestamp.from(Instant.now()));
    }

    private boolean checkCreateOpportunity(TemplateEntity template) {
        if (CollectionUtils.isEmpty(template.getDependentTemplates())) {
            if (CollectionUtils.isEmpty(template.getDetails()))
                return true;
            else
                return Type.RECURRING.equals(template.getType());
        } else {
            return true;
        }
    }

    private ExpenseDetailEntity createGeneralIncomeDetail(TemplateEntity template) {
        return new ExpenseDetailEntity()
                .setTemplateId(template.getId())
                .setPeriod(template.getPeriod())
                .setName(template.getName())
                .setCurrency(template.getCurrency())
                .setPaid(false);
    }

    private ExpenseDetailEntity fillGeneralIncomeDetail(TemplateEntity template, ExpenseDetailEntity detail) {
        return detail
                .setName(template.getName())
                .setItemId(template.getItemId())
                //todo сделать перерасчет порядкового номера, так как сейчас, если первый платеж в 28 числа, а новый добавляемый 26,
                // то он встанет вторым, хотя 26 идет раньше 28. Сделать возможность вставки данных в промежутаках
                .setOrder(nextOrderForDetailExpense(template))
                .setOperationType(template.getOperationType())
                .setPeriod(template.getPeriod())
                .setPlanPaymentDate(convertOrNull(dateService.nextPaymentDate(template)));
    }

    private void fillRecurringDetailAndSave(TemplateEntity template, ExpenseDetailEntity detail) {
        detail.setPlanPaymentDate(convertOrNull(dateService.nextPaymentDate(template)));
        expenseDetailRepository.save(detail);
    }

    private void fillAmount(TemplateEntity template, ExpenseDetailEntity newDetail) throws ServiceException {
        //todo добавить создание новой записи без суммы и рассчитать автоматически как наступит период
        double sum = 0.0;
        if (!CollectionUtils.isEmpty(template.getDependentTemplates()) || template.isAllDetails()) {
            YearMonth nowPeriod = YearMonth.now();
            YearMonth requiredCalculationPeriod = nowPeriod.plusMonths(template.getTemplatePeriod().getOffset());
            if (CollectionUtils.isEmpty(template.getDetails())) {
                if (template.isAllDetails())
                    newDetail.setAmount(calculatePercent(calculateTotalAmount(template, requiredCalculationPeriod), template.getPercent()));
                else
                    newDetail.setAmount(calculatePercent(calculateTemplateAmount(template, requiredCalculationPeriod), template.getPercent()));
            } else {
                ExpenseDetailEntity lastPaidDetail = template.getDetails().getLast(); // todo последний детайил можеть быть не последней по дате
                YearMonth lastPaidPeriod = YearMonth.from(lastPaidDetail.getFactPaymentDate().toLocalDateTime());
                boolean timeHasCome = lastPaidPeriod.isBefore(nowPeriod);
                if (timeHasCome) {
                    if (template.isAllDetails())
                        newDetail.setAmount(calculatePercent(calculateTotalAmount(template, requiredCalculationPeriod), template.getPercent()));
                    else
                        newDetail.setAmount(calculatePercent(calculateTemplateAmount(template, requiredCalculationPeriod), template.getPercent()));
                } else {
                    //планирваоние суммы
                    newDetail
                            .setAmount(lastPaidDetail.getAmount())
                            .setLoadAmountDate(dateService.getFirstDayOfMonth(lastPaidPeriod, template.getTemplatePeriod().getOffset()));
                }
            }
        } else if (template.getTemplatePeriod() != null) {
            if (template.getTemplatePeriod().equals(PaymentPeriod.LAST_MONTH)) {
                YearMonth date = YearMonth.from(LocalDate.now().minusMonths(1));
                List<ExpenseDetailEntity> details = expenseDetailRepository.findByPaymentDateAndPaid(date.getMonthValue(), date.getYear());
                sum = details.stream().mapToDouble(ExpenseDetailEntity::getAmount).sum();
                newDetail.setAmount(calculatePercent(sum, template.getPercent()));
            }
//            else if (template.getTemplatePeriod().equals(TemplatePeriod.EXPENSE_LAST_MONTH)) {
//                //todo not implemented
//            }
        } else {
            newDetail.setAmount(template.getAmount());
        }
    }

    private boolean checkDetailHasPaidInPeriod(TemplateEntity dependTemplate, ValidityPeriod period) throws ServiceException {
        if (dependTemplate == null)
            return false;

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

    private Double calculatePercent(Double amount, Double percent) throws ServiceException {
        if (amount == null || percent == null)
            throw new ServiceException("Amount and percent must not be null");
        double result = (amount * percent) / 100;
        return Math.round(result * 100) / 100.0;
    }

    private Double calculateTemplateAmount(TemplateEntity template, YearMonth lastPaidPeriod) throws ServiceException {
        double sum = 0.0;
        for (TemplateEntity dependentTemplate : template.getDependentTemplates()) { //todo подумать над тем что счета могут быть в разной валюте
            if (CollectionUtils.isEmpty(dependentTemplate.getDetails()))
                continue;
            if (template.getCurrency() == null)
                template.setCurrency(dependentTemplate.getCurrency()); //todo тип валюты берется из перого шаблона, если она будет отличется что это не будет учтено
            for (ExpenseDetailEntity dependDetail : dependentTemplate.getDetails()) {
                if (Boolean.TRUE.equals(dependDetail.isPaid())
                        && dateService.dateIsIncludeInPeriod(dependDetail.getFactPaymentDate(), dateService.getRangePeriod(lastPaidPeriod)))
                    sum += dependDetail.getAmount();
            }
        }
        return sum;
    }

    private Double calculateTotalAmount(TemplateEntity template, YearMonth date) {
        double sum = 0.0;
        List<ExpenseDetailEntity> totalDetail = expenseDetailRepository.findByFactPaymentDateLikeYearMonthAndPaid(date.getMonthValue(), date.getYear());
        for (ExpenseDetailEntity detail : totalDetail) {
            sum += detail.getAmount();
        }
        if (template.getCurrency() == null)
            template.setCurrency(totalDetail.getFirst().getCurrency());
        return sum;
    }

    private void fillingFixedDetailAndSave(TemplateEntity template, ExpenseDetailEntity detail) throws ServiceException {
        if (template.getPaymentCount() > 1)
            expenseDetailRepository.saveAll(fillExpenseDetailList(template));
        else {
            detail
                    .setAmount(template.getDetailAmount())//todo странное заполение amount. Заоление происходит в методе выше по иерархии
                    .setPlanPaymentDate(template.getPaymentDate());
            expenseDetailRepository.save(detail);
        }
    }

    private ExpenseDetailEntity createGeneralDetail(TemplateEntity template) {
        return new ExpenseDetailEntity()
                .setName(template.getName())
                .setItemId(template.getItemId())
                .setOperationType(template.getOperationType())
                .setTemplateId(template.getId())
                .setCurrency(template.getCurrency())
                .setPeriod(template.getPeriod())
                .setPaid(false);
    }

    private List<ExpenseDetailEntity> fillExpenseDetailList(TemplateEntity template) throws ServiceException {
        List<ExpenseDetailEntity> list = new ArrayList<>();
        Timestamp nextPaymentDate = template.getPaymentDate();
        AmountPerMonth amountPerMonth = amountPerMonth(template);
        for (int i = 1; i <= template.getPaymentCount(); i++) {
            if (i != 1) {
                nextPaymentDate = convertOrNull(dateService.nextPaymentDateForCredit(template, nextPaymentDate));
                if (i == 2)
                    template.setPaymentInCurrentMonth(false);
            }
            ExpenseDetailEntity entity = createGeneralDetail(template);
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
        depositService.changeDepositAmount(dbDetail, false);
        dbDetail
                .setPaid(false)
                .setFactPaymentDate(null)
                .setDepositId(null);
//        historyService.createHistoryTransaction(dbDetail, HistoryTransactionEntity.OperationStatus.CANCEL);
        expenseDetailRepository.save(dbDetail);
    }

    public List<ExpenseDetailEntity> getExpenseDetailsForCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        //todo заменить поиском
        List<ExpenseDetailEntity> byPaymentDateInCurrentMonth = expenseDetailRepository.findByPaymentDate(currentMonth, currentYear);
        byPaymentDateInCurrentMonth.sort(Comparator.comparing(ExpenseDetailEntity::getPlanPaymentDate));
        return byPaymentDateInCurrentMonth;
    }

    public List<ExpenseDetailEntity> getIncomeDetails() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        List<ExpenseDetailEntity> byPaymentDateInCurrentMonth = expenseDetailRepository.findByPaymentDateAndNotPaid(currentMonth, currentYear);
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
