package org.aturkov.expense.dto.template;

import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.dao.template.TemplateEntity;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.TemplatePeriod;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.dto.DTOConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.aturkov.expense.domain.ValidityPeriod.Time.NONE;


@Data
@Accessors(chain = true)
public class TemplateDTOv1 {
    public String name;
    public Double amount;
    public Double percent;
    public CurrencyType currency;
    public TemplateEntity.OperationType operationType;
    public TemplateEntity.Type type;
    public ValidityPeriod.Time period;
    public TemplatePeriod templatePeriod;
    public Integer paymentCount;
    public Integer paymentDay;
    public Boolean weekend;
    public UUID dependTemplateId;
    public UUID depositId;
    @DateTimeFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDate paymentDate;
    @DateTimeFormat(pattern = DTOConfig.DATA_TIME_FORMAT)
    public LocalDateTime expiryDate;
    @DateTimeFormat(pattern = DTOConfig.DATA_TIME_FORMAT)
    public LocalDateTime createAt;
}
