package org.aturkov.expense.dto.item;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.itemgroup.ItemGroupEntity;

@Data
@Accessors(chain = true)
public class ItemSaveDTOv1 {
    public String name;
    public ItemGroupEntity itemGroup;
}
