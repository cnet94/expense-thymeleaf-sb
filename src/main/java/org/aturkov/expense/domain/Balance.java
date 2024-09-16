package org.aturkov.expense.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Currency;

@Data
@Accessors(chain = true)
public class Balance {
    private String totalAmount;
    private String remainderAmount;
    private CurrencyType currency;
}
