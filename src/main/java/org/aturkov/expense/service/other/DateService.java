package org.aturkov.expense.service.other;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.domain.PaymentPeriod;
import org.aturkov.expense.exception.ServiceException;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.ValidityPeriod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.*;


@Service
@RequiredArgsConstructor
public class DateService {
    public void checkDateInFuture(Timestamp date) throws ServiceException {
        if (LocalDate.now().isAfter(date.toLocalDateTime().toLocalDate()))
            throw new ServiceException("The date cannot be in the past");
    }

    public boolean checkExpiryDate(TemplateEntity expense) {
        if (expense.getExpiryDate() == null && expense.getPaymentDay() == null)
            return true;
        LocalDate expiryDate = expense.getExpiryDate().toLocalDateTime().toLocalDate();
        LocalDate nextPaymentDate = LocalDate.now().plusMonths(1).withDayOfMonth(expense.getPaymentDay());
        return nextPaymentDate.isBefore(expiryDate);
    }

    public boolean dateIsIncludeInPeriod(Timestamp timestamp, ValidityPeriod period) throws ServiceException {
        return dateIsIncludeInPeriod(convertToLocaleDate(timestamp), period);
    }

    public boolean dateIsIncludeInPeriod(LocalDate localDate, ValidityPeriod period) throws ServiceException {
        return localDate.isAfter(period.getDateFrom()) && localDate.isBefore(period.getDateTo());
    }

    public boolean checkActualPaymentPeriod(TemplateEntity expense) {
        if (expense.getPeriod() == PaymentPeriod.NONE)
            return true;
        int currentOffset = PaymentPeriod.getCurrentOffset(expense.getPeriod());
        Timestamp paymentDate = expense.getPaymentDate();
//        ValidityPeriod validityPeriod = fillingPeriod(paymentDate, currentOffset);
//        Timestamp from = Timestamp.valueOf(validityPeriod.getDateFrom());
//        Timestamp to = Timestamp.valueOf(validityPeriod.getDateTo());
        return false;

//        if (currentOffset == ValidityPeriod.Time.getCurrentOffset(LAST_MONTH)) {
//            // todo позволять оплачивать рекурентные платежи на перед. сейчас нет возможности полатить если месяц не закончился
//            ValidityPeriod validityPeriod = calculatePeriod(now, 0);
//            Timestamp from = Timestamp.valueOf(validityPeriod.getFrom());
//            Timestamp to = Timestamp.valueOf(validityPeriod.getTo());
//            return expenseDetailRepository.existsByExpenseIdAndPaidAndPlanPaymentDateBetween(template.getId(), true, from, to);
//        }
//        return true;
    }

    public void fillRangePeriod(ExpenseDetailEntity detail) {
        if (detail.getPeriod() == null)
            return;
        YearMonth targetMonth = YearMonth.from(detail.getPlanPaymentDate().toLocalDateTime()).plusMonths(detail.getPeriod().getOffset());
        detail
                .setPeriodDateFrom(convertOrNull(targetMonth.atDay(1)))
                .setPeriodDateTo(convertOrNull(targetMonth.atEndOfMonth()));
    }

    public ValidityPeriod getRangePeriod(YearMonth date) {;
        return new ValidityPeriod()
                .setDateFrom(date.atDay(1))
                .setDateTo(date.atEndOfMonth());
    }

    public Timestamp getFirstDayOfMonth(YearMonth date, long period) {
        return Timestamp.valueOf(date.minusMonths(period).atDay(1).atStartOfDay());
    }

    // проверка
    public void checkIncomeLastMonthAlreadyFinished(TemplateEntity template) throws ServiceException {
        if (template.getDetails() == null) {
            int offsetPeriod = template.getPeriod().getOffset();
            LocalDate periodLocaleDate = convertToLocaleDate(template.getPaymentDate()).minusMonths(offsetPeriod);
            YearMonth periodYearMonth = YearMonth.from(periodLocaleDate);

        }

        // проверяем что месяц за который хотим получить сумму уже закончен
        // берем у зависимого шаблона период
        YearMonth lastDate = YearMonth.from(LocalDateTime.now());
        YearMonth paymentDate = YearMonth.from(template.getPaymentDate().toLocalDateTime()).minusMonths(template.getPeriod().getOffset());
        if (!lastDate.equals(paymentDate))
            throw new ServiceException("The detail cannot be created because the period for which the calculation is to be made has not ended.");
    }

    public LocalDate nextPaymentDate(TemplateEntity template) {
        //todo do other periods (now ONE month)
        boolean weekend = template.getWeekend();
        int paymentDay = template.getPaymentDay();

        if (!CollectionUtils.isEmpty(template.getDetails())) {
            ExpenseDetailEntity detail = template.getDetails().stream()
                    .filter(e -> e.getOrder().equals(template.getDetails().size()))
                    .findFirst().orElseThrow();
            return correctDateOfNextPayment(detail.getPlanPaymentDate(),  paymentDay, weekend, 1);
        }
        if (template.getPaymentDate() == null) {
            return correctDateOfNextPayment(null, paymentDay, weekend, 0);
        } else {
            return correctDateOfNextPayment(template.getPaymentDate(), paymentDay, weekend, 0);
        }
        //todo проверка, существует ли оплаченный платеж в текущем месяце, если да, то плюсуется месяц
//        boolean ret = expenseDetailRepository.existsByExpenseIdAndPlanPaymentDateAndPaid(template.getId(), planPaymentDate, true);
//        if (ret)
//            return convertToTimestamp(planDate.plusMonths(1));
//        else
//            return planPaymentDate;
    }

    public LocalDate nextPaymentDateForCredit(TemplateEntity template, Timestamp lastPaymentDate) {
        //todo support different period (user choose), now supported only ONE MONTH (Release 2)
        int planPaymentDay = template.getPaymentDay();
        boolean weekend = template.getWeekend();

        // если текщий платеж является вторым в месяце и включен режим расчет с учетом выходных (оплата до выходных),
        // проверяется что второй платеж не совершится в тот же день (что и платеж перед ним)
        if (template.isPaymentInCurrentMonth()) {
            LocalDate realPaymentDate = correctDateOfNextPayment(lastPaymentDate, planPaymentDay, weekend, 0);
            if (realPaymentDate.isAfter(lastPaymentDate.toLocalDateTime().toLocalDate())) {
                return realPaymentDate;
            }
        }
        return correctDateOfNextPayment(lastPaymentDate, planPaymentDay, weekend, 1);
    }

    private LocalDate correctDateOfNextPayment(Timestamp paymentDate, int paymentDay, boolean weekend, int countMonth) {
        if (paymentDate == null) {
            YearMonth date = YearMonth.from(LocalDateTime.now()).plusMonths(countMonth);
            int lastDayOfMonth = date.lengthOfMonth();
            int realPaymentDay = Math.min(paymentDay, lastDayOfMonth);
            return adjustPaymentDate(date.atDay(realPaymentDay), weekend);
        } else {
            if (countMonth > 0) {
                YearMonth date = YearMonth.from(convertToLocaleDate(paymentDate)).plusMonths(countMonth);
                int lastDayOfMonth = date.lengthOfMonth();
                int realPaymentDay = Math.min(paymentDay, lastDayOfMonth);
                return adjustPaymentDate(date.atDay(realPaymentDay), weekend);
            }
            return adjustPaymentDate(convertToLocaleDate(paymentDate), weekend);
        }
    }

    private static LocalDate adjustPaymentDate(LocalDate paymentDate, boolean weekend) {
        if (!weekend)
            return paymentDate;
        DayOfWeek dayOfWeek = paymentDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY)
            return paymentDate.minusDays(1);
        else if (dayOfWeek == DayOfWeek.SUNDAY)
            return paymentDate.minusDays(2);
        else
            return paymentDate;
    }

    public void checkPaymentCountPositiveAndLimit(TemplateEntity expense, int numberExpenseDetails) throws ServiceException {
        Integer count = expense.getPaymentCount();
        if (count < 1 || count > numberExpenseDetails)
            throw new ServiceException("Payment count must be positive or extra number expectation[" + numberExpenseDetails + "] -> actual[" + count + "]");
    }

    public void checkPaymentDateNotNull(TemplateEntity expense) throws ServiceException {
        if (expense.getPaymentDate() == null)
            throw new ServiceException("Date wasn't be null");
    }

    public void checkPaymentDayCorrect(TemplateEntity expense) throws ServiceException {
        if (expense.getPaymentDay() > 31 || expense.getPaymentDay() < 1)
            throw new ServiceException("Payment day in not correct");
    }

    public static double roundDouble(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    public static Timestamp convertOrNull(LocalDate localDate) {
        return localDate != null ? Timestamp.valueOf(localDate.atStartOfDay()) : null;
    }

    public static Timestamp convertOrNull(LocalDateTime localDateTime) {
        return localDateTime != null ? Timestamp.valueOf(localDateTime) : null;
    }

    public static LocalDate convertToLocaleDate(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime().toLocalDate() : null;
    }

    public static LocalDateTime convertToLocaleDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

}
