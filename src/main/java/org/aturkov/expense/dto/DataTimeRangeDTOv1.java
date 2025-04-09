package org.aturkov.expense.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DataTimeRangeDTOv1 {
    public LocalDateTime from;
    public LocalDateTime to;
}
