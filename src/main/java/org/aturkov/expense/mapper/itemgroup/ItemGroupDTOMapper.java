package org.aturkov.expense.mapper.itemgroup;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dao.itemgroup.ItemGroupEntity;
import org.aturkov.expense.dto.item.ItemDTOv1;
import org.aturkov.expense.dto.itemgroup.ItemGroupDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemGroupDTOMapper extends SimpleDTOMapper<ItemGroupEntity, ItemGroupDTOv1> {

    @Override
    public void map(ItemGroupEntity src, ItemGroupDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setName(src.getName());
    }
}
