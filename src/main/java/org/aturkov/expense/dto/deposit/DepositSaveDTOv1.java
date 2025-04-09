package org.aturkov.expense.dto.deposit;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.domain.CurrencyType;


@Data
@Accessors(chain = true)
public class DepositSaveDTOv1 {
    public String name;
    public Double amount;
    public CurrencyType currency;
    public DepositEntity.Status status;
}
