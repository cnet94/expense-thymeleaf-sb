package org.aturkov.expense.dto;

import lombok.Data;

@Data
public class AmountRangeDTOv1 {
    public Double amountMore;
    public Double amountEquals;
    public Double amountLess;
}
