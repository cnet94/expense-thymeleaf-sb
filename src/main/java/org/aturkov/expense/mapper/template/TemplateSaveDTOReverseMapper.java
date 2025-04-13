package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.TemplateSaveDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.service.other.DateService;
import org.springframework.stereotype.Component;


import static org.aturkov.expense.service.other.DateService.convertOrNull;

@Component
@RequiredArgsConstructor
public class TemplateSaveDTOReverseMapper extends SimpleDTOMapper<TemplateSaveDTOv1, TemplateEntity> {

    @Override
    public void map(TemplateSaveDTOv1 src, TemplateEntity dst, MapperContext mapperContext) {
        dst
                .setName(src.getName())
                .setPeriod(src.getPeriod())
                .setAmount(src.getAmount() == null ? null : src.getAmount())
                .setPercent(src.getPercent() == null ? null : src.getPercent())
                .setCurrency(src.getCurrency())
                .setItemId(src.getItemId())
                .setOperationType(src.getOperationType())
                .setType(src.getType())
                .setPaymentCount(src.getPaymentCount())
                .setPaymentDay(src.getPaymentDay())
                .setWeekend(src.getWeekend())
                .setDependentTemplateIds(src.getDependTemplateIds())
                .setAllDetails(src.isAllDetails())
                .setTemplatePeriod(src.getTemplatePeriod())
                .setDepositId(src.getDepositId())
                .setPaymentInCurrentMonth(src.isPaymentInCurrentMonth())
                .setPaymentDate(DateService.convertOrNull(src.getPaymentDate()))
                .setExpiryDate(convertOrNull(src.getExpiryDate()))
                .setCreatedAt(convertOrNull(src.getCreateAt()));
    }
}
