package org.aturkov.expense.domain;

import lombok.Getter;

@Getter
public enum CurrencyType {
    USD("доллар"),
    BYN("бел. руб."),
    RUB("рос. руб.");

    private final String value;

    CurrencyType(String value) {
        this.value = value;
    }
}
