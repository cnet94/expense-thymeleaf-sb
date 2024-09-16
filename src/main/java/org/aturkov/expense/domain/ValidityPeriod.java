package org.aturkov.expense.domain;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@Accessors(chain = true)
public class ValidityPeriod {
    public LocalDateTime from;
    public LocalDateTime to;

    @Getter
    public enum Time {
        LAST_MONTH(-1),
        CURRENT_MONTH(0),
        NEXT_MONTH(1);

        private final int offset;

        Time(int offset) {
            this.offset = offset;
        }

        public static int currentOffset(Time type) {
            return Arrays.stream(Time.values()).filter(t -> t.equals(type)).findAny().orElse(CURRENT_MONTH).getOffset();
        }
    }
}
