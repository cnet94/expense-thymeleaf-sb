package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.TemplateListDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateDTOMapperV2 extends SimpleDTOMapper<TemplateEntity, TemplateListDTOv1> {

    @Override
    public void map(TemplateEntity src, TemplateListDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setName(src.getName());
    }
}
