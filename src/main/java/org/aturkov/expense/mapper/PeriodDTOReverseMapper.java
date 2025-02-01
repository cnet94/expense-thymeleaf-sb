package org.aturkov.expense.mapper;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.domain.ValidityPeriodDTOv1;
import org.aturkov.expense.service.DateService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PeriodDTOReverseMapper extends SimpleDTOMapper<ValidityPeriodDTOv1, ValidityPeriod> {

    @Override
    public void map(ValidityPeriodDTOv1 src, ValidityPeriod dst, MapperContext mapperContext) throws Exception {
        dst
                .setDateFrom(src.getDateFrom())
                .setDateTo(src.getDateTo());
    }
}
