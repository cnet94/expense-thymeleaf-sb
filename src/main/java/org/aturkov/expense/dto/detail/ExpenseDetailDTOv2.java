package org.aturkov.expense.dto.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.ValidityPeriod;
import org.aturkov.expense.dto.DTOConfig;
import org.aturkov.expense.dto.ValidityPeriodDTOv1;
import org.aturkov.expense.dto.item.ItemDTOv1;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExpenseDetailDTOv2 extends ExpenseDetailDTOv1 {
    public ItemDTOv1 item;
}
