package org.aturkov.cambium.pagination.util;

import java.util.function.BinaryOperator;

public class FunctionalUtils {
    public static <T> BinaryOperator<T> defaultParallelAccumulatorOperator(Class<T> clazz) {
        return (a, b) -> a;
    }
}
