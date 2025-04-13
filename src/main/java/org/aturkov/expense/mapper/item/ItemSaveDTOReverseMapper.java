package org.aturkov.expense.mapper.item;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dto.item.ItemSaveDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemSaveDTOReverseMapper extends SimpleDTOMapper<ItemSaveDTOv1, ItemEntity> {

    @Override
    public void map(ItemSaveDTOv1 src, ItemEntity dst, MapperContext mapperContext) throws Exception {
        dst
                .setName(src.getName())
                .setOperationType(src.getOperationType());
    }
}
