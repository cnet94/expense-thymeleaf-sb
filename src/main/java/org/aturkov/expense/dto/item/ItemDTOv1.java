package org.aturkov.expense.dto.item;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.domain.OperationType;

import java.util.UUID;


@Data
@Accessors(chain = true)
public class ItemDTOv1 {
    public UUID id;
    public String name;
    public ItemEntity.Status status;
    public OperationType operationType;
}
