package org.aturkov.expense.domain;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Arrays;

@Data
@Accessors(chain = true)
public class ValidityPeriod {
    public LocalDate dateFrom;
    public LocalDate dateTo;

    @Getter
    public enum Time {
        NONE("Не выбрано", 0),
        LAST_MONTH("Прошлый месяц",-1),
        CURRENT_MONTH("Текущий месяц",0),
        NEXT_MONTH("Следующий месяц",1);

        private final String name;
        private final int offset;

        Time(String name, int offset) {
            this.name = name;
            this.offset = offset;
        }

        public static String getCurrentName(Time time) {
            return Arrays.stream(Time.values()).filter(t -> t.equals(time)).findFirst().orElse(NONE).getName();
        }

        public static int getCurrentOffset(Time time) {
            return Arrays.stream(Time.values()).filter(t -> t.equals(time)).findFirst().orElse(NONE).getOffset();
        }

        public static Time getTime(String time) {
            return Arrays.stream(Time.values()).filter(t -> t.name.equals(time)).findFirst().orElse(NONE);
        }
    }
}
