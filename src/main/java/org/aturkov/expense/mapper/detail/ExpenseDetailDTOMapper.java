package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.domain.PaymentPeriod;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.dto.detail.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.mapper.other.PeriodDTOMapper;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

import static org.aturkov.expense.service.other.DateService.convertToLocaleDate;
import static org.aturkov.expense.service.other.DateService.convertToLocaleDateTime;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOMapper extends SimpleDTOMapper<ExpenseDetailEntity, ExpenseDetailDTOv1> {

    private final PeriodDTOMapper periodDTOMapper;
    private final ItemDTOMapper itemDTOMapper;

    @Override
    public void map(ExpenseDetailEntity src, ExpenseDetailDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setOperationType(src.getOperationType())
                .setItemId(src.getItemId())
                .setTemplateId(src.getTemplateId())
                .setOrder(src.getOrder())
                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrency(src.getCurrency())
                .setPeriod(src.getPeriod())
                .setPlanPaymentDate(convertToLocaleDate(src.getPlanPaymentDate()))
                .setFactPaymentDate(convertToLocaleDateTime(src.getFactPaymentDate()))
                .setUpdateAmountDate(convertToLocaleDate(src.getLoadAmountDate()))
                .setPaid(src.isPaid());
        if (src.getFactPaymentDate() != null && src.getPeriod() != PaymentPeriod.NONE) {
            src
                    .setValidityPeriod(new ValidityPeriod()
                            .setDateFrom(convertToLocaleDate(src.getPeriodDateFrom()))
                            .setDateTo(convertToLocaleDate(src.getPeriodDateTo())));
            dst
                    .setPaymentPeriod(periodDTOMapper.convert(src.getValidityPeriod()));
        }
        dst
                .setItem(itemDTOMapper.convert(src.getItem()));
        //todo ApiUser
//        if (entity.getCurrency() != CURRENCY) {
//            expenseDetailDTOv1
//                    .set(convertCurrency(entity.getAmount()))
//                    .setCurrencyCountry(CURRENCY);
//        }
    }
}
