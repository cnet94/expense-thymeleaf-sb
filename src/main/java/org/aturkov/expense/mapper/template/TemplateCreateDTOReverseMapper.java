package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.TemplateCreateDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateCreateDTOReverseMapper extends SimpleDTOMapper<TemplateCreateDTOv1, TemplateEntity> {

    private final TemplateSaveDTOReverseMapper templateSaveDTOReverseMapper;

    @Override
    public void map(TemplateCreateDTOv1 src, TemplateEntity dst, MapperContext mapperContext) {
        templateSaveDTOReverseMapper.map(src, dst, mapperContext);
    }
}
