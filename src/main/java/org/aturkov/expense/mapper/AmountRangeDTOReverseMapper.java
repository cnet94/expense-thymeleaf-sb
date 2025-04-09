package org.aturkov.expense.mapper;

import org.aturkov.expense.domain.AmountRange;
import org.aturkov.expense.dto.AmountRangeDTOv1;
import org.springframework.stereotype.Component;

@Component
public class AmountRangeDTOReverseMapper extends SimpleDTOMapper<AmountRangeDTOv1, AmountRange> {

    @Override
    public void map(AmountRangeDTOv1 src, AmountRange dst, MapperContext mapperContext) throws Exception {
        dst
                .setAmountMore(src.getAmountMore())
                .setAmountEquals(src.getAmountEquals())
                .setAmountLess(src.getAmountLess());
    }
}
