package org.aturkov.expense.dto.pagination;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaginationDTOv1 {
    private int offset;
    private int limit;
    private long total;

    public PaginationDTOv1() {
    }
}
