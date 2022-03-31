package com.dstym.pharmaciesondutyattica.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class DateUtils {
    public static Date getDateFromTodayPlusDays(int days) {
        var now = LocalDateTime.now().plusDays(days);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date stringToDate(String stringDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
        }

        return null;
    }

    public static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static Instant stringDateToInstant(String date) {
        try {
            if (date != null) {
                return Instant.parse(date + "T00:00:00Z");
            }

            return null;
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
            return null;
        }
    }

    public static String instantToString(Instant instant) {
        try {
            return instant.toString().substring(0, instant.toString().indexOf("T"));
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
            return "";
        }
    }
}
