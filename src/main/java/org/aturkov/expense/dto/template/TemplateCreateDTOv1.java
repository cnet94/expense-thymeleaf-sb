package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TemplateCreateDTOv1 extends TemplateSaveDTOv1 {
}
