package org.aturkov.expense.domain;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Arrays;

@Data
@Accessors(chain = true)
public class ValidityPeriod {
    public LocalDate dateFrom;
    public LocalDate dateTo;
}
