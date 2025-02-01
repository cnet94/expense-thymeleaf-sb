package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;


@Data
@Accessors(chain = true)
public class TemplateListDTOv1 {
    public UUID id;
    public String name;
}
