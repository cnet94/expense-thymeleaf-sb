package org.aturkov.expense.mapper.item;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dto.item.ItemDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.mapper.itemgroup.ItemGroupDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemDTOMapper extends SimpleDTOMapper<ItemEntity, ItemDTOv1> {

    private final ItemGroupDTOMapper itemGroupDTOMapper;

    @Override
    public void map(ItemEntity src, ItemDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setItemGroupId(src.getItemGroupId())
                .setName(src.getName())
                .setStatus(src.getStatus())
                .setItemGroup(itemGroupDTOMapper.convert(src.getItemGroup()));
    }
}
