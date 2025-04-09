package org.aturkov.expense.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class DataRange {
    private Timestamp from;
    private Timestamp to;
}
