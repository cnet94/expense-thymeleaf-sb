package org.aturkov.expense.mapper.item;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dto.item.ItemCreateDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemSaveDTOMapper extends SimpleDTOMapper<ItemCreateDTOv1, ItemEntity> {

    @Override
    public void map(ItemCreateDTOv1 src, ItemEntity dst, MapperContext mapperContext) throws Exception {
        


    }
}
