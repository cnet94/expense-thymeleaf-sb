package org.aturkov.expense.dto.itemgroup;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class ItemGroupMapSaveDTOv1 {
    public UUID itemGroupId;
    public UUID itemId;
}
