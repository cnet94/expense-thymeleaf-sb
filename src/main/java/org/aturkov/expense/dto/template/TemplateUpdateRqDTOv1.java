package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TemplateUpdateRqDTOv1 extends Request {
    public TemplateSaveDTOv1 detail;
}
