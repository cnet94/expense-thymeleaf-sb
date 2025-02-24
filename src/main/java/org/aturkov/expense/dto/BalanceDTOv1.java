package org.aturkov.expense.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;

@Data
@Accessors(chain = true)
public class BalanceDTOv1 {
    public Double totalAmount;
    public Double remainderAmount;
    public CurrencyType currency;
}
