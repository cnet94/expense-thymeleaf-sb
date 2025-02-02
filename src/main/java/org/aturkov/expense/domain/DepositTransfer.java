package org.aturkov.expense.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.deposit.DepositEntity;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DepositTransfer {
    private LocalDateTime transferDate;
    private DepositEntity from;
    private DepositEntity to;
    private Double amount;
    private String description;
}
