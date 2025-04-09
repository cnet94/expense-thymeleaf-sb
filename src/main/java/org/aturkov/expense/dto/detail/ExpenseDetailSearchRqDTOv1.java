package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.PaymentPeriod;
import org.aturkov.expense.dto.Request;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExpenseDetailSearchRqDTOv1 extends Request {
    public Set<UUID> idList;
    public Set<UUID> idExcludeList;
    public Set<UUID> templateIdList;
    public Set<UUID> templateIdExcludeList;
    public Set<String> nameLikeList;
    public Set<String> nameNotLikeList;
    public Set<CurrencyType> currencyTypeList;
    public Set<CurrencyType> currencyTypeExcludeList;
    public Set<PaymentPeriod> periodList;
    public Set<PaymentPeriod> periodExcludeList;
//    public AmountRangeDTOv1 amountRange;
    public LocalDate planDateFrom;
    public LocalDate planDateTo;
//    public Ternary paid;
}
