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
    /**
     * Calculates a `Date` object representing the current date plus a specified number of days.
     * <p>
     * This method adds the given number of days to the current date and time,
     * then converts the resulting `LocalDateTime` to a `Date` object using the system's default time zone.
     *
     * @param days the number of days to add to the current date.
     * @return a `Date` object representing the calculated date.
     */
    public static Date getDateFromTodayPlusDays(int days) {
        var now = LocalDateTime.now().plusDays(days);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts a string representation of a date to a `Date` object.
     * <p>
     * This method parses a date string in the format "yyyy-MM-dd" into a `Date` object.
     * If an exception occurs during parsing, it logs the error and returns null.
     *
     * @param stringDate the string representation of the date in "yyyy-MM-dd" format.
     * @return a `Date` object representing the parsed date, or null if an error occurs.
     */
    public static Date stringToDate(String stringDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
        }

        return null;
    }

    /**
     * Converts a `Date` object to its string representation in "yyyy-MM-dd" format.
     * <p>
     * This method uses `SimpleDateFormat` to format the given `Date` object
     * into a string representation of the date.
     *
     * @param date the `Date` object to be converted to a string.
     * @return a string representation of the date in "yyyy-MM-dd" format.
     */
    public static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * Converts a string representation of a date to an `Instant` object.
     *
     * @param date the string representation of the date in "yyyy-MM-dd" format.
     * @return an `Instant` object representing the date, or null if the input is null
     * or an error occurs during parsing.
     */
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

    /**
     * Converts an `Instant` object to a string representation of the date.
     *
     * @param instant the `Instant` object to be converted to a string.
     * @return a string representation of the date in "yyyy-MM-dd" format, or an empty string if an error occurs.
     */
    public static String instantToString(Instant instant) {
        try {
            return instant.toString().substring(0, instant.toString().indexOf("T"));
        } catch (Exception exception) {
            log.error(ExceptionUtils.getStackTrace(exception));
            return "";
        }
    }
}
