package org.aturkov.expense.domain;

import lombok.Getter;

@Getter
public enum Type {
    RECURRING ("Регулярный", 2), // полуавтоматизированный, следующая операция создается на основе шаблона
    FIXED("Фиксированный", 3); // автоматизированный, оперции созаются все и сразу

    private final String alias;
    private final int order;

    Type(String alias, int order) {
        this.alias = alias;
        this.order = order;
    }
}
