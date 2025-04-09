package org.aturkov.expense.dto.itemgroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ItemGroupMapCreateRqDTOv1 extends Request {
    public ItemGroupMapSaveDTOv1 itemGroupMap;
}
