package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Response;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExpenseDetailSaveRsDTOv1 extends Response {
    public ExpenseDetailDTOv1 detail;
}
