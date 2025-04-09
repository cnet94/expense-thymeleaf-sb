package org.aturkov.expense.mapper;

import org.aturkov.expense.domain.DataRange;
import org.aturkov.expense.dto.DataRangeDTOv1;
import org.aturkov.expense.service.other.DateService;
import org.springframework.stereotype.Component;

import static org.aturkov.expense.service.other.DateService.convertOrNull;


@Component
public class DataRangeDTOReverseMapper extends SimpleDTOMapper<DataRangeDTOv1, DataRange> {

    @Override
    public void map(DataRangeDTOv1 src, DataRange dst, MapperContext mapperContext) throws Exception {
        dst
                .setFrom(DateService.convertOrNull(src.getFrom()))
                .setTo(DateService.convertOrNull(src.getTo()));
    }
}
