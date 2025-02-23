package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TemplateSaveDTOv1 extends TemplateDTOv1 {
    boolean paymentInCurrentMonth = false;
}
