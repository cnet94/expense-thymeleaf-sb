package org.aturkov.expense.mapper.expenseDetail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.expenseDetail.ExpenseDetailEntity;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.dto.expenseDetail.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.PeriodDTOMapper;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

import static org.aturkov.expense.service.DateService.convertToLocaleDate;
import static org.aturkov.expense.service.DateService.convertToLocaleDateTime;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOMapper extends SimpleDTOMapper<ExpenseDetailEntity, ExpenseDetailDTOv1> {

    private final PeriodDTOMapper periodDTOMapper;

    @Override
    public void map(ExpenseDetailEntity src, ExpenseDetailDTOv1 dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setTemplateId(src.getTemplateId())
                .setOrder(src.getOrder())
                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrency(src.getCurrency())
                .setPeriod(src.getPeriod())
                .setPlanPaymentDate(convertToLocaleDate(src.getPlanPaymentDate()))
                .setFactPaymentDate(convertToLocaleDateTime(src.getFactPaymentDate()))
                .setPaid(src.isPaid());
        if (src.getFactPaymentDate() != null && src.getPeriod() != ValidityPeriod.Time.NONE) {
            src
                    .setValidityPeriod(new ValidityPeriod()
                            .setDateFrom(convertToLocaleDate(src.getPeriodDateFrom()))
                            .setDateTo(convertToLocaleDate(src.getPeriodDateTo())));
            dst
                    .setPaymentPeriod(periodDTOMapper.convert(src.getValidityPeriod()));
        }
        //todo ApiUser
//        if (entity.getCurrency() != CURRENCY) {
//            expenseDetailDTOv1
//                    .set(convertCurrency(entity.getAmount()))
//                    .setCurrencyCountry(CURRENCY);
//        }
    }
}
