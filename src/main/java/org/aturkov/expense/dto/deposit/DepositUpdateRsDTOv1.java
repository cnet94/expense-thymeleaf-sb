package org.aturkov.expense.dto.deposit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Response;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DepositUpdateRsDTOv1 extends Response {
    public DepositDTOv1 deposit;
}
