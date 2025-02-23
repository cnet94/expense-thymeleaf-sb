package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailDTOv2;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOMapperV2 extends SimpleDTOMapper<ExpenseDetailEntity, ExpenseDetailDTOv2> {

    private final ExpenseDetailDTOMapper expenseDetailDTOMapper;
    private final ItemDTOMapper itemDTOMapper;

    @Override
    public void map(ExpenseDetailEntity src, ExpenseDetailDTOv2 dst, MapperContext mapperContext) throws Exception {
        expenseDetailDTOMapper.map(src, dst, mapperContext);

//        dst
//                .setItem(itemDTOMapper.convert(src.getItem(), mapperContext));
    }
}
