package org.aturkov.expense.dto.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.OperationType;
import org.aturkov.expense.domain.PaymentPeriod;
import org.aturkov.expense.dto.ValidityPeriodDTOv1;
import org.aturkov.expense.dto.DTOConfig;
import org.aturkov.expense.dto.item.ItemDTOv1;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExpenseDetailDTOv1 {
    @JsonIgnore
    public UUID id;
    public OperationType operationType;
    public UUID itemId;
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
    public PaymentPeriod period;
    public ValidityPeriodDTOv1 paymentPeriod;
    public boolean paid;
    public ItemDTOv1 item;
}
