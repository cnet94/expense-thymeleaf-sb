package org.aturkov.expense.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.ValidityPeriod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExpenseDTOv1 {
    public UUID id;

    public String name;

    public String type;

    private ValidityPeriod.Time period;

    public BalanceDTOv1 generalBalance;

    public BalanceDTOv1 remainderBalance;

    public int paymentDay;

    @JsonFormat(pattern = DTOConfig.DATE_FORMAT)
    public LocalDateTime expiryDate;

    @JsonFormat(pattern = DTOConfig.FULL_DATE_FORMAT)
    public LocalDateTime createAt;

    public List<ExpenseDetailDTOv1> details;
}
