package org.aturkov.expense.mapper.other;

import org.aturkov.expense.dto.DTOConfig;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DTOConfig.DATE_FORMAT);

    @Override
    public LocalDateTime convert(String source) {
        if (source.trim().isEmpty()) {
            return null;
        }
        LocalDate date = LocalDate.parse(source, formatter);
        return LocalDateTime.of(date, LocalTime.MIN);
    }
}