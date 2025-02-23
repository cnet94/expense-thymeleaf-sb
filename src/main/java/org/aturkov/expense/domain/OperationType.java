package org.aturkov.expense.domain;

import lombok.Getter;

@Getter
public enum OperationType {
    INCOME("Доход"),
    EXPENSE("Расход");
//        TRANSFER("Трансфер");

    private final String alias;

    OperationType(String alias) {
        this.alias = alias;
    }
}
