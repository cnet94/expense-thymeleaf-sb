package org.aturkov.expense.mapper.template;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.domain.PaymentPeriod;
import org.aturkov.expense.dto.template.TemplateRsDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.mapper.other.BalanceDTOMapper;
import org.aturkov.expense.mapper.detail.ExpenseDetailDTOMapper;
import org.aturkov.expense.service.template.TemplateService;
import org.springframework.stereotype.Component;


import static org.aturkov.expense.service.other.DateService.convertToLocaleDate;
import static org.aturkov.expense.service.other.DateService.convertToLocaleDateTime;

@Component
@RequiredArgsConstructor
public class TemplateDTOMapper extends SimpleDTOMapper<TemplateEntity, TemplateRsDTOv1> {

    private final ExpenseDetailDTOMapper expenseDetailDTOMapper;
    private final BalanceDTOMapper balanceDTOMapper;
    private final TemplateService templateService;

    @Override
    public void map(TemplateEntity src, TemplateRsDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setName(src.getName())
                .setCurrency(src.getCurrency())
                .setOperationType(src.getOperationType())
                .setType(src.getType().getAlias())
                .setDepositId(src.getDepositId())
                .setPeriod(PaymentPeriod.getCurrentName(src.getPeriod()))
                .setPaymentCount(src.getPaymentCount())
                .setPaymentDay(src.getPaymentDay() != null ? src.getPaymentDay() : null)
                .setWeekend(src.getWeekend())
                .setPaymentDate(convertToLocaleDate(src.getPaymentDate()))
                .setExpiryDate(convertToLocaleDateTime(src.getExpiryDate()))
                .setCreateAt(convertToLocaleDateTime(src.getCreatedAt()));
        fillAmount(src, dst);
        if (src.getDetails() != null)
            dst.setDetails(expenseDetailDTOMapper.convertCollection(src.getDetails()));
    }

    private void fillAmount(TemplateEntity src, TemplateRsDTOv1 dst) throws Exception {
        dst.setAmount(src.getAmount());
        switch (src.getType()) {
            case RECURRING -> {}
            case FIXED -> {
                templateService.fillingGeneralBalance(src);
                dst.setBalance(balanceDTOMapper.convert(src.getGeneralBalance()));
            }
        }
    }
}
