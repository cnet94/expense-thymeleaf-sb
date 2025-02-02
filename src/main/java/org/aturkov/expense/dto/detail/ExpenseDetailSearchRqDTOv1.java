package org.aturkov.expense.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aturkov.expense.domain.CurrencyType;
import org.aturkov.expense.domain.Ternary;
import org.aturkov.expense.domain.ValidityPeriod;
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
    public Set<Double> amountMoreList;
    public Set<Double> amountEqualsList;
    public Set<Double> amountLessList;
    public Set<CurrencyType> currencyTypeList;
    public Set<CurrencyType> currencyTypeExcludeList;
    public LocalDate planPaymentDate;
    public ValidityPeriod.Time periodType;
    public ValidityPeriod planValidityPeriod;
    public Ternary paid;
}
