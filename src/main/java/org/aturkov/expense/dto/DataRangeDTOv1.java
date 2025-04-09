package org.aturkov.expense.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DataRangeDTOv1 {
    public LocalDate from;
    public LocalDate to;
}
