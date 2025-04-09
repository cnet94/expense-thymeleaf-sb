package org.aturkov.expense.mapper.itemgroup;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.itemgroup.ItemGroupMapEntity;
import org.aturkov.expense.dto.itemgroup.ItemGroupMapDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemGroupMapDTOMapper extends SimpleDTOMapper<ItemGroupMapEntity, ItemGroupMapDTOv1> {

    @Override
    public void map(ItemGroupMapEntity src, ItemGroupMapDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setItemGroupId(src.getItemGroupId())
                .setItemId(src.getItemId());
    }
}
