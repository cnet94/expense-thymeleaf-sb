package org.aturkov.expense.mapper.pagination;

import org.aturkov.cambium.pagination.PaginationResult;
import org.aturkov.expense.dto.pagination.PaginationDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
public class PaginationMapper extends SimpleDTOMapper<PaginationResult<?>, PaginationDTOv1> {
    @Override
    public void map(PaginationResult<?> src, PaginationDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setOffset(src.getOffset())
                .setLimit(src.getLimit())
                .setTotal(src.getTotal());
    }
}
