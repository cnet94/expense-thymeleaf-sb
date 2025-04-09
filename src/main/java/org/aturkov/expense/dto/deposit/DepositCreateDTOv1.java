package org.aturkov.expense.dto.deposit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DepositCreateDTOv1 extends DepositSaveDTOv1 {
}
