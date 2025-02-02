package org.aturkov.expense.dto.deposit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DepositUpdateRqDTOv1 extends DepositSaveDTOv1 {
    public UUID id;
}
