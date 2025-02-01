package org.aturkov.expense.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Currency;

@Data
@Accessors(chain = true)
public class Balance {
    private Double totalAmount;
    private Double remainderAmount;
    private CurrencyType currency;
}
