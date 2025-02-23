package org.aturkov.expense.dto.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ItemCreateDTOv1 extends ItemSaveDTOv1 {
}
