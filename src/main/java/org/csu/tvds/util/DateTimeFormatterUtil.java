package org.csu.tvds.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Kwanho
 * @date 2022-10-30 12:27
 * LocalDate格式化类
 */
public class DateTimeFormatterUtil {

    private static final String DEFAULT_FORMATTER = "yyyy年MM月dd日 HH:mm:ss";
    private static final String ACCURATE_TO_DAY = "yyyy年MM月dd日";

    public static String format(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FORMATTER);
        if (dateTime == null) {
            return "";
        }
        return dateTimeFormatter.format(dateTime);
    }

    public static String accurateToDay(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ACCURATE_TO_DAY);
        if (dateTime == null) {
            return "";
        }
        return dateTimeFormatter.format(dateTime);
    }
}
