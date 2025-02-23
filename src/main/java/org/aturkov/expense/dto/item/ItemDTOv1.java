package org.aturkov.expense.dto.item;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dto.itemgroup.ItemGroupDTOv1;

import java.util.UUID;


@Data
@Accessors(chain = true)
public class ItemDTOv1 {
    public UUID id;
    public UUID itemGroupId;
    public String name;
    public ItemEntity.Status status;
    public ItemGroupDTOv1 itemGroup;
}
