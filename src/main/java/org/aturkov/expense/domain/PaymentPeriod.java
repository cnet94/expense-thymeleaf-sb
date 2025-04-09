package org.aturkov.expense.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PaymentPeriod {
    NONE("Не выбрано", 0),
    LAST_MONTH("Прошлый месяц",-1),
    CURRENT_MONTH("Текущий месяц",0),
    NEXT_MONTH("Следующий месяц",1);

    private final String name;
    private final int offset;

    PaymentPeriod(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    public static String getCurrentName(PaymentPeriod time) {
        return Arrays.stream(PaymentPeriod.values()).filter(t -> t.equals(time)).findFirst().orElse(NONE).getName();
    }

    public static int getCurrentOffset(PaymentPeriod time) {
        return Arrays.stream(PaymentPeriod.values()).filter(t -> t.equals(time)).findFirst().orElse(NONE).getOffset();
    }

    public static PaymentPeriod getTime(String time) {
        return Arrays.stream(PaymentPeriod.values()).filter(t -> t.name.equals(time)).findFirst().orElse(NONE);
    }
}
