package org.aturkov.expense.mapper.itemgroup;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.itemgroup.ItemGroupMapEntity;
import org.aturkov.expense.dto.itemgroup.ItemGroupMapDTOv2;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemGroupMapDTOMapperV2 extends SimpleDTOMapper<ItemGroupMapEntity, ItemGroupMapDTOv2> {

    private final ItemGroupMapDTOMapper itemGroupMapDTOMapper;
    private final ItemGroupDTOMapper itemGroupDTOMapper;
    private final ItemDTOMapper itemDTOMapper;

    @Override
    public void map(ItemGroupMapEntity src, ItemGroupMapDTOv2 dst, MapperContext mapperContext) throws Exception {
        itemGroupMapDTOMapper.map(src, dst, mapperContext);
        dst
                .setItemGroup(itemGroupDTOMapper.convert(src.getItemGroup(), mapperContext))
                .setItem(itemDTOMapper.convert(src.getItem(), mapperContext));
    }
}
