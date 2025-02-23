package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.dto.Request;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TemplateCreateRqDTOv1 extends Request {
    public TemplateCreateDTOv1 template;
}
