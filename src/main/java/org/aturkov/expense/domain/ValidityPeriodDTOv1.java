package org.aturkov.expense.domain;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.DTOConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Arrays;

@Data
@Accessors(chain = true)
public class ValidityPeriodDTOv1 {
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDate dateFrom;
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDate dateTo;
}
