package com.dstym.pharmaciesondutyattica.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static Date getDateFromTodayPlusDays(int days) {
        LocalDateTime now = LocalDateTime.now().plusDays(days);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date stringToDate(String stringDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
