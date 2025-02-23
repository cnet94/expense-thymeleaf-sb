package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Response;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExpenseDetailListRsDTOv1 extends Response {
    public List<ExpenseDetailDTOv2> details;
}
