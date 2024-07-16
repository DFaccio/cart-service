package com.cart_service.util.pagination.time;

import java.time.*;
import java.time.format.DateTimeFormatter;

public interface TimeUtils {

    static ZoneId getZoneId() {
        return ZoneId.of("America/Sao_Paulo");
    }

    static LocalDateTime getDate(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    static LocalDateTime getTime() {
        return LocalDateTime.now()
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .toLocalDateTime();
    }


    static LocalDateTime now() {
        Clock clock = Clock.system(getZoneId());

        return LocalDateTime.now(clock);
    }

    static long getDurationBetweenInSeconds(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toSeconds();
    }

}