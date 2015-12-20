package com.fyelci.sorumania.util;

import java.time.Instant;
import java.time.ZonedDateTime;

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

}
