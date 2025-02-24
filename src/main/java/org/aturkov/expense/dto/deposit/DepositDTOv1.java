package org.aturkov.expense.dto.deposit;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.domain.CurrencyType;

import java.util.UUID;


@Data
@Accessors(chain = true)
public class DepositDTOv1 {
    public UUID id;
    public String name;
    public Double amount;
    public CurrencyType currency;
    public DepositEntity.Status status;
}
