package org.aturkov.expense.mapper.expenseDetail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.expenseDetail.ExpenseDetailEntity;
import org.aturkov.expense.dto.expenseDetail.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.PeriodDTOReverseMapper;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import static org.aturkov.expense.service.DateService.convertToTimestamp;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailDTOv1, ExpenseDetailEntity> {

    private final PeriodDTOReverseMapper periodDTOReverseMapper;

    @Override
    public void map(ExpenseDetailDTOv1 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setTemplateId(src.getTemplateId())
                .setDependDetailId(src.getDependDetailId())
                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrency(src.getCurrency())
                .setPeriod(src.getPeriod())
                .setPlanPaymentDate(convertToTimestamp(src.getPlanPaymentDate()))
                .setOrder(src.getOrder())
                .setValidityPeriod(periodDTOReverseMapper.convert(src.getPaymentPeriod()))
                .setPaid(src.isPaid());
        if (src.getFactPaymentDate() != null)
            dst.setFactPaymentDate(Timestamp.valueOf(src.getFactPaymentDate()));
    }
}
