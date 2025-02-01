package org.aturkov.expense.dto.expenseDetail;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.domain.ValidityPeriodDTOv1;
import org.aturkov.expense.dto.DTOConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExpenseDetailDTOv1 {
    public UUID id;
    public UUID templateId;
    public UUID dependDetailId;
    public Integer order;
    public String name;
    public Double amount;
    public CurrencyType currency;
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDate planPaymentDate;
    public LocalDateTime factPaymentDate;
    public LocalDateTime paymentDate;
    public ValidityPeriod.Time period;
    public ValidityPeriodDTOv1 paymentPeriod;
    public boolean paid;
}
