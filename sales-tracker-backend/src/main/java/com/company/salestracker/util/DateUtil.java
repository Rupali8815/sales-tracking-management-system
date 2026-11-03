package com.company.salestracker.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil() {}

    public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String format(LocalDate date) {
        return date == null ? null : date.format(DISPLAY_FORMAT);
    }

    public static LocalDate firstDayOfMonth(int month, int year) {
        return YearMonth.of(year, month).atDay(1);
    }

    public static LocalDate lastDayOfMonth(int month, int year) {
        return YearMonth.of(year, month).atEndOfMonth();
    }
}
