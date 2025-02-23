package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.TemplateSaveDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;


import static org.aturkov.expense.service.other.DateService.convertToTimestamp;

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
                .setDependTemplateId(src.getDependTemplateId())
                .setTemplatePeriodId(src.getTemplatePeriod())
                .setDepositId(src.getDepositId())
                .setPaymentInCurrentMonth(src.isPaymentInCurrentMonth())
                .setPaymentDate(convertToTimestamp(src.getPaymentDate()))
                .setExpiryDate(convertToTimestamp(src.getExpiryDate()))
                .setCreatedAt(convertToTimestamp(src.getCreateAt()));
    }
}
