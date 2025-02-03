package org.aturkov.expense.service.template;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dao.template.TemplateRepository;
import org.aturkov.expense.domain.Balance;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.service.other.DateService;
import org.aturkov.expense.service.EntitySecureFindServiceImpl;
import org.aturkov.expense.service.EntitySmartService;
import org.aturkov.expense.service.detail.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.aturkov.expense.service.other.DateService.roundDouble;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateService extends EntitySecureFindServiceImpl<TemplateEntity> {
    private final TemplateRepository templateRepository;
    private final DateService dateService;
    @Lazy
    @Autowired
    private DetailService detailService;

    @Override
    public boolean validateEntity(TemplateEntity entity, EntitySmartService.EntityValidateMode entityValidateMode) throws ServiceException {
        return true;
    }

    public TemplateEntity safeFindTemplate(UUID uuid, FindMode findMode) throws ServiceException {
        return safeFindEntity(uuid, templateRepository, findMode);
    }

    public List<TemplateEntity> getAllThatIsActive() {
        return templateRepository.findAllByActiveIsTrue();
    }

    public List<TemplateEntity> getAllByIdIn(Collection<UUID> ids) {
        return templateRepository.findAllByIdIn(ids);
    }

    public TemplateEntity getTemplateWithSortDetail(UUID id) throws ServiceException {
        TemplateEntity dbTemplate = safeFindTemplate(id, FindMode.ifNullThrowsError);
        sortingDetails(dbTemplate);
        return dbTemplate;
    }

    public TemplateEntity getTemplateByDetailIdWithSort(UUID detailId) throws ServiceException {
        ExpenseDetailEntity dbExpenseDetail = detailService.findExpenseDetail(detailId);
        TemplateEntity dbTemplate = safeFindTemplate(dbExpenseDetail.getTemplateId(), FindMode.ifNullThrowsError);
        sortingDetails(dbTemplate);
        return dbTemplate;
    }

    public List<TemplateEntity> getIncomeTemplate() throws ServiceException {
        return templateRepository.findByOperationType(TemplateEntity.OperationType.INCOME);
    }

    private void sortingDetails(TemplateEntity dbTemplate) {
        if (dbTemplate.getDetails() != null)
            dbTemplate.getDetails().sort((e1, e2) -> {
                if (e1.isPaid() == e2.isPaid()) {
                    return e1.isPaid()
                            ? e2.getPlanPaymentDate().compareTo(e1.getPlanPaymentDate()) // paid=true → по убыванию даты
                            : e1.getPlanPaymentDate().compareTo(e2.getPlanPaymentDate()); // paid=false → по возрастанию даты
                }
                return Boolean.compare(e1.isPaid(), e2.isPaid());
            });
    }

    @Transactional
    public void archiveTemplate(UUID id) {
        archiveTemplate(Collections.singletonList(id));
    }

    @Transactional
    public void archiveTemplate(List<UUID> listIds) {
        if (CollectionUtils.isEmpty(listIds))
            return;
        List<TemplateEntity> dbTemplateList = getAllByIdIn(listIds);
        dbTemplateList.forEach(e -> e.setActive(false));
        for (UUID detailId : listIds) {
            log.info("template[{}] perhaps was mark as archive", detailId);
        }
    }

    @Transactional
    public void deleteTemplate(UUID id) {
        deleteTemplate(Collections.singletonList(id));
    }

    @Transactional
    public void deleteTemplate(List<UUID> listIds) {
        if (CollectionUtils.isEmpty(listIds))
            return;
        templateRepository.deleteAllByIdIn(listIds);
        for (UUID detailId : listIds) {
            log.info("template[{}] perhaps was deleted", detailId);
        }
    }

    public void updateTemplate(TemplateEntity templateEntity) {
        templateRepository.save(templateEntity);
    }

    public TemplateEntity createTemplate(TemplateEntity template) throws ServiceException {
        validationTemplate(template);
        fillingTemplate(template);
        TemplateEntity dbTemplate = templateRepository.save(template);
        dbTemplate.setPaymentInCurrentMonth(template.isPaymentInCurrentMonth());
        detailService.createDetail(dbTemplate);
        return dbTemplate;
    }

    private void fillingTemplate(TemplateEntity template) throws ServiceException {
        fillingGeneralTemplate(template);
        fillingAmountOrPercent(template);

        switch (template.getType()) {
            case BASIC -> fillingTemplateOneTime(template);
            case RECURRING -> fillingTemplateRecurring(template);
            case FIXED -> fillingTemplateCredit(template);
//            case INCOME -> fillingIncome(template);
        }
    }

    private void fillingAmountOrPercent(TemplateEntity template) throws ServiceException {
        if (template.getAmount() != null) {
            template
                    .setAmount(template.getAmount())
                    .setCurrency(template.getCurrency())
                    .setPercent(null);
        } else {
            template
                    .setAmount(null)
                    .setCurrency(template.getDependTemplate().getCurrency());
        }
    }

    private void fillingGeneralTemplate(TemplateEntity template) {
        template
                .setOperationType(template.getOperationType())
                .setDetails(new ArrayList<>())
                .setActive(true);;
    }

    private void fillingIncome(TemplateEntity template) {

    }

    private void fillingTemplateCredit(TemplateEntity template) {
        if (template.getPeriod() == ValidityPeriod.Time.NONE)
            template.setPeriod(ValidityPeriod.Time.CURRENT_MONTH);
        template
                .setDetailAmount(template.getAmount())
                .setAmount(null);
        fillingTemplateReminder(template);
    }

    private void fillingTemplateRecurring(TemplateEntity template) {
//        template
//                .setPaymentCount(null);
    }

    private void fillingTemplateOneTime(TemplateEntity template) throws ServiceException {
        template
                //todo check where user getDetailAmount
//                .setDetailAmount(template.getAmount())
                .setPaymentCount(null)
                .setPaymentDay(null)
                .setTempPaymentDate(template.getPaymentDate())
                .setPaymentDate(null)
                .setExpiryDate(null);
    }

    private void validationTemplate(TemplateEntity template) throws ServiceException {
        if (templateRepository.existsByName(template.getName()))
            throw new ServiceException("Expense with same name already exists");
        if (template.getPaymentDate() != null)
            dateService.checkDateInFuture(template.getPaymentDate());
        if (template.getExpiryDate() != null)
            dateService.checkDateInFuture(template.getExpiryDate());
        switch (template.getType()) {
            case BASIC -> {
                if (template.getAmount() == null &&  template.getPercent() == null)
                    throw new ServiceException("Amount or percent not be null");
            }
            case RECURRING -> {
                checkAmountPositive(template);
                if (template.getPaymentDay() == null)
                    throw new ServiceException("Payment day is required");
            }
            case FIXED -> {
                checkAmountPositive(template);
                dateService.checkPaymentDayCorrect(template);
                dateService.checkPaymentCountPositiveAndLimit(template, 100);
                dateService.checkPaymentDateNotNull(template);
            }
        }
    }

    public void fillingGeneralBalance(TemplateEntity templateEntity) {
        templateEntity.setGeneralBalance(new Balance()
                .setTotalAmount(countTotal(templateEntity.getDetails()))
                .setRemainderAmount(countBalanceOfPayment(templateEntity.getDetails()))
                .setCurrency(templateEntity.getCurrency()));
    }

    private Double countTotal(List<ExpenseDetailEntity> list) {
        return list.stream().mapToDouble(ExpenseDetailEntity::getAmount).sum();
    }

    private Double countBalanceOfPayment(List<ExpenseDetailEntity> list) {
        return list.stream()
                .filter(e -> !e.isPaid())
                .mapToDouble(ExpenseDetailEntity::getAmount)
                .sum();
    }

    private void checkAmountPositive(TemplateEntity expense) throws ServiceException {
        if (expense.getAmount() != null && expense.getAmount() < 0)
            throw new ServiceException("Amount must be positive");
    }

    private void fillingTemplateReminder(TemplateEntity template) {
        double amount = template.getDetailAmount();
        Integer count = template.getPaymentCount();
        double avg = roundDouble(amount / count);
        double reminder = roundDouble(amount - (avg * (count - 1)));
        if (reminder != 0)
            template.setReminder(reminder);
        else
            template.setReminder(0.0);
    }
}
