package com.fyelci.sorumania.util;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fatih on 20/12/15.
 */
public class DateUtil {

    public static java.util.Date toJavaUtilDateFromZonedDateTime ( ZonedDateTime zdt ) {
        Instant instant = zdt.toInstant();
        long millisecondsSinceEpoch = instant.toEpochMilli();  // Data-loss, going from nanosecond resolution to milliseconds.
        java.util.Date date = new java.util.Date( millisecondsSinceEpoch );
        return date;
    }


    public static Date addDays(Date date, int dayCount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, dayCount);
        return cal.getTime();
    }

    public static Date subtractDays(Date date, int dayCount) {
        return addDays(date, -dayCount);
    }
}
