package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.dto.BalanceDTOv1;
import org.aturkov.expense.dto.DTOConfig;
import org.aturkov.expense.dto.detail.ExpenseDetailDTOv1;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class TemplateRsDTOv1 {
    public UUID id;
    public Boolean expense;
    public String name;
    public TemplateEntity.OperationType operationType;
    public TemplateEntity.Type type;
    public String period;
    public Double amount;
    public CurrencyType currency;
    public BalanceDTOv1 balance;
    public BalanceDTOv1 remainderBalance;
    public Integer paymentDay;
    public Integer paymentCount;
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDate paymentDate;
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDateTime expiryDate;
    @DateTimeFormat(pattern = DTOConfig.DATA_TIME_FORMAT)
    public LocalDateTime createAt;
    public List<ExpenseDetailDTOv1> details;
}
