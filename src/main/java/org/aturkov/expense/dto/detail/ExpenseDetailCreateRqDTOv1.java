package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExpenseDetailCreateRqDTOv1 extends Request {
    public ExpenseDetailSaveDTOv1 detail;
}
