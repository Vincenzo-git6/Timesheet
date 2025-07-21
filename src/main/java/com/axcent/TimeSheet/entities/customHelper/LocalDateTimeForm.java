package com.axcent.TimeSheet.entities.customHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Questa classe si occupa della formattazione del giorno e dell'ora
 */
public class  LocalDateTimeForm
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String format(LocalDateTime data) {
        if(data==null) return "";
        return data.format(FORMATTER);
    }

    public static String now() {
        return format(LocalDateTime.now());
    }
}
