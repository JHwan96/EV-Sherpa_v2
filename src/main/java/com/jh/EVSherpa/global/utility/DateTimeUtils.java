package com.jh.EVSherpa.global.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static LocalDateTime dateTimeFormat(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(dateStr, formatter);
    }

}
