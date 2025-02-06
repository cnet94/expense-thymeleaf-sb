package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.dto.template.TemplateCreateDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;


import static org.aturkov.expense.service.other.DateService.convertToTimestamp;

@Component
@RequiredArgsConstructor
public class TemplateCreateRqDTOReverseMapper extends SimpleDTOMapper<TemplateCreateDTOv1, TemplateEntity> {

    @Override
    public void map(TemplateCreateDTOv1 src, TemplateEntity dst, MapperContext mapperContext) {
        dst
                .setOperationType(src.getOperationType())
                .setType(src.getType())
                .setName(src.getName())
                .setPeriod(src.getPeriod())
                .setAmount(src.getAmount() == null ? null : src.getAmount())
                .setPercent(src.getPercent() == null ? null : src.getPercent())
                .setCurrency(src.getCurrency())
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
