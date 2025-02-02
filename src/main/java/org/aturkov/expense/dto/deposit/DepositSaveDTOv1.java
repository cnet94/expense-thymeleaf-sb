package org.aturkov.expense.dto.deposit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.deposit.DepositEntity;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.dto.Request;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DepositSaveDTOv1 extends Request {
    public String name;
    public Double amount;
    public CurrencyType currencyType;
    public DepositEntity.Status status;
}
