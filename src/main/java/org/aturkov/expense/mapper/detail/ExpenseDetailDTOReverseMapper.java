package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.other.PeriodDTOReverseMapper;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.service.other.DateService;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import static org.aturkov.expense.service.other.DateService.convertOrNull;

@Component
@RequiredArgsConstructor
public class ExpenseDetailDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailDTOv1, ExpenseDetailEntity> {

    private final PeriodDTOReverseMapper periodDTOReverseMapper;

    @Override
    public void map(ExpenseDetailDTOv1 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        dst
                .setId(src.getId())
                .setOperationType(src.getOperationType())
                .setItemId(src.getItemId())
                .setTemplateId(src.getTemplateId())
                .setDependDetailId(src.getDependDetailId())
                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrency(src.getCurrency())
                .setPeriod(src.getPeriod())
                .setPlanPaymentDate(DateService.convertOrNull(src.getPlanPaymentDate()))
                .setOrder(src.getOrder())
                .setValidityPeriod(periodDTOReverseMapper.convert(src.getPaymentPeriod()))
                .setPaid(src.isPaid());
        if (src.getFactPaymentDate() != null)
            dst.setFactPaymentDate(Timestamp.valueOf(src.getFactPaymentDate()));
    }
}
