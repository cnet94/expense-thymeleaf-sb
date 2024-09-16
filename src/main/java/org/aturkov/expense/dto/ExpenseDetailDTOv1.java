package org.aturkov.expense.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.ValidityPeriod;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExpenseDetailDTOv1 {
    public UUID id;
    public UUID expenseId;
    public String name;
    public Double amount;
    public CurrencyType currency;
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDateTime planPaymentDate;
    public LocalDateTime factPaymentDate;
    public LocalDateTime paymentDate;
    public ValidityPeriod.Time period;
    @JsonFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDateTime paymentPeriodFrom;
    @JsonFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDateTime paymentPeriodTo;
    public boolean paid;
}
