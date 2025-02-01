package org.aturkov.expense.mapper.other;

import java.util.Currency;

public class FormatMapper {
    public static final Double CONVERSION = 3.15;
    public static final Currency CURRENCY = Currency.getInstance("BYN");

    public static String doubleToString(double value) {
        return String.format("%.2f", value);
    }

    public static String convertCurrency(double value) {
        return doubleToString(value * CONVERSION);
    }
}
