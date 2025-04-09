package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExpenseDetailUpdateDTOv1 extends ExpenseDetailSaveDTOv1 {
}
