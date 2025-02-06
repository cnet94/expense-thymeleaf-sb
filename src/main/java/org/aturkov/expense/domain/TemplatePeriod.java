package org.aturkov.expense.domain;

import lombok.Getter;

@Getter
public enum TemplatePeriod {
    INCOME_LAST_MONTH("Доход за предыдущий месяц"),
    EXPENSE_LAST_MONTH("Расход за предыдущий месяц");

    private final String alias;

    TemplatePeriod(String alias) {
        this.alias = alias;
    }
}
