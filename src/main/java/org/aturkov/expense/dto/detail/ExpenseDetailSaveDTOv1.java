package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.OperationType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExpenseDetailSaveDTOv1 {
    public OperationType operationType;
    public UUID itemId;
    public UUID templateId;
    public String name;
    public Double amount;
    public CurrencyType currency;
    public LocalDate planPaymentDate;
}
