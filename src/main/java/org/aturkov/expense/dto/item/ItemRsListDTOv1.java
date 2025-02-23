package org.aturkov.expense.dto.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Response;
import org.aturkov.expense.dto.detail.ExpenseDetailDTOv2;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ItemRsListDTOv1 extends Response {
    public List<ItemDTOv1> items;
}
