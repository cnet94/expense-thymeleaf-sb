package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TemplateUpdateDTOv1 extends TemplateDTOv1 {
    public UUID id;
}
