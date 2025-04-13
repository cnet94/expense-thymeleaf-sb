package org.aturkov.expense.dto.item;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.itemgroup.ItemGroupEntity;
import org.aturkov.expense.domain.OperationType;

@Data
@Accessors(chain = true)
public class ItemSaveDTOv1 {
    public String name;
    public OperationType operationType;
    public ItemGroupEntity itemGroup;
}
