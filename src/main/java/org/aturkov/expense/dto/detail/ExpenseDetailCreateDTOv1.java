package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ExpenseDetailCreateDTOv1 extends ExpenseDetailSaveDTOv1 {
}
