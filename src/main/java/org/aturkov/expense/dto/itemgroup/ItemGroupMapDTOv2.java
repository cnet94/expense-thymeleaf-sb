package org.aturkov.expense.dto.itemgroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.item.ItemDTOv1;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ItemGroupMapDTOv2 extends ItemGroupMapDTOv1{
    public ItemGroupDTOv1 itemGroup;
    public ItemDTOv1 item;
}
