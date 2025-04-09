package org.aturkov.expense.dto.deposit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DepositCreateRqDTOv1 extends Request {
    public DepositCreateDTOv1 deposit;
}
