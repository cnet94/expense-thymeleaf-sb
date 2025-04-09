package org.aturkov.expense.dto.itemgroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Response;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ItemGroupMapRsListDTOv1 extends Response {
    public List<ItemGroupMapDTOv2> itemGroupMaps;
}
