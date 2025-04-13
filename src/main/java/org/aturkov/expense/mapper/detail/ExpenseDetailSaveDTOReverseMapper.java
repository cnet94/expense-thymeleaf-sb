package org.aturkov.expense.mapper.detail;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.detail.ExpenseDetailEntity;
import org.aturkov.expense.dto.detail.ExpenseDetailSaveDTOv1;
import org.aturkov.expense.mapper.MapperContext;
import org.aturkov.expense.mapper.SimpleDTOMapper;
import org.aturkov.expense.service.other.DateService;
import org.springframework.stereotype.Component;

import static org.aturkov.expense.service.other.DateService.convertOrNull;

@Component
@RequiredArgsConstructor
public class ExpenseDetailSaveDTOReverseMapper extends SimpleDTOMapper<ExpenseDetailSaveDTOv1, ExpenseDetailEntity> {

    @Override
    public void map(ExpenseDetailSaveDTOv1 src, ExpenseDetailEntity dst, MapperContext mapperContext) throws Exception {
        dst
                .setTemplateId(src.getTemplateId())
                .setOperationType(src.getOperationType())
                .setItemId(src.getItemId())
                .setName(src.getName())
                .setAmount(src.getAmount())
                .setCurrency(src.getCurrency())
                .setPlanPaymentDate(DateService.convertOrNull(src.getPlanPaymentDate()))
                .setAutoCreatePlanPaymentDate(src.isAutoCreatePlanPaymentDate());
    }
}
