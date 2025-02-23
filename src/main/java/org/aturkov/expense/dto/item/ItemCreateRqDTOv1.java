package org.aturkov.expense.dto.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ItemCreateRqDTOv1 extends Request {
    public ItemCreateDTOv1 item;
}
