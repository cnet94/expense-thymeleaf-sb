package org.aturkov.expense.mapper;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.domain.ValidityPeriodDTOv1;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PeriodDTOMapper extends SimpleDTOMapper<ValidityPeriod, ValidityPeriodDTOv1> {

    @Override
    public void map(ValidityPeriod src, ValidityPeriodDTOv1 dst, MapperContext mapperContext) throws Exception {
            dst
                    .setDateFrom(src.getDateFrom())
                    .setDateTo(src.getDateTo());
        }
}
