package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.pagination.PaginationDTOv1;

import java.util.List;

@Data
@Accessors(chain = true)
public class ExpenseDetailSearchRsDTOv1 {
    public PaginationDTOv1 pagination;
    public List<ExpenseDetailDTOv2> details;
}
