package org.aturkov.expense.mapper.item;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dto.item.ItemDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemDTOMapper extends SimpleDTOMapper<ItemEntity, ItemDTOv1> {

    @Override
    public void map(ItemEntity src, ItemDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setName(src.getName())
                .setStatus(src.getStatus());
    }
}
