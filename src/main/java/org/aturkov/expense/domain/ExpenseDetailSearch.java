package org.aturkov.expense.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExpenseDetailSearch {
    private Set<UUID> idList;
    private Set<UUID> idExcludeList;
    private Set<UUID> templateIdList;
    private Set<UUID> templateIdExcludeList;
    private Set<String> nameLikeList;
    private Set<String> nameNotLikeList;
    private Set<CurrencyType> currencyTypeList;
    private Set<CurrencyType> currencyTypeExcludeList;
    private Set<PaymentPeriod> periodList;
    private Set<PaymentPeriod> periodExcludeList;
    private AmountRange amountRange;
    private DataRange planData;
    private Ternary paid;
}
