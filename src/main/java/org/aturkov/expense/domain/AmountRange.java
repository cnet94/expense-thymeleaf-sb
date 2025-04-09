package org.aturkov.expense.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AmountRange {
    private Double amountMore;
    private Double amountEquals;
    private Double amountLess;
}
