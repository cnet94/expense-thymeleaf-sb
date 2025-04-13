package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.DependTemplateDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateDTOMapperV2 extends SimpleDTOMapper<TemplateEntity, DependTemplateDTOv1> {

    @Override
    public void map(TemplateEntity src, DependTemplateDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setName(src.getName());
    }
}
