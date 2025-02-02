package org.aturkov.expense.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ValidityPeriodDTOv1 {
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDate dateFrom;
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDate dateTo;
}
