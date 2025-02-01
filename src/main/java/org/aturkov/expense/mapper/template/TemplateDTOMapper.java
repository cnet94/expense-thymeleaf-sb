package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.dto.template.TemplateRsDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.mapper.other.BalanceDTOMapper;
import org.aturkov.expense.mapper.expenseDetail.ExpenseDetailDTOMapper;
import org.aturkov.expense.service.TemplateService;
import org.springframework.stereotype.Component;


import static org.aturkov.expense.service.DateService.convertToLocaleDate;
import static org.aturkov.expense.service.DateService.convertToLocaleDateTime;

@Component
@RequiredArgsConstructor
public class TemplateDTOMapper extends SimpleDTOMapper<TemplateEntity, TemplateRsDTOv1> {

    private final ExpenseDetailDTOMapper expenseDetailDTOMapper;
    private final BalanceDTOMapper balanceDTOMapper;
    private final TemplateService templateService;

    @Override
    public void map(TemplateEntity src, TemplateRsDTOv1 dst, MapperContext mapperContext) throws Exception {
        templateService.fillingGeneralBalance(src);
        dst
                .setId(src.getId())
                .setName(src.getName())
                .setAmount(src.getAmount() != null ? src.getDetailAmount() : null)
                .setCurrency(src.getCurrency())
                .setOperationType(src.getOperationType())
                .setType(src.getType())
                .setPeriod(ValidityPeriod.Time.getCurrentName(src.getPeriod()))
                .setPaymentCount(src.getPaymentCount())
                .setPaymentDay(src.getPaymentDay() != null ? src.getPaymentDay() : null)
                .setBalance(balanceDTOMapper.convert(src.getGeneralBalance()))
                .setPaymentDate(convertToLocaleDate(src.getPaymentDate()))
                .setExpiryDate(convertToLocaleDateTime(src.getExpiryDate()))
                .setCreateAt(convertToLocaleDateTime(src.getCreateAt()));
        if (src.getDetails() != null)
            dst.setDetails(expenseDetailDTOMapper.convertCollection(src.getDetails()));
    }
}
