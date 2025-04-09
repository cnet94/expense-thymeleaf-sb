package org.aturkov.expense.dto.itemgroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.item.ItemSaveDTOv1;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ItemGroupMapCreateDTOv1 extends ItemGroupMapSaveDTOv1 {
}
