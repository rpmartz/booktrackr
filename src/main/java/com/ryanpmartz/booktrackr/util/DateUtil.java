package com.ryanpmartz.booktrackr.util;

import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static Date toJavaDate(LocalDate localDate) {
        Assert.notNull(localDate, "Cannot convert null LocalDate to Date");
        Instant localDateInstant = Instant.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()));

        return Date.from(localDateInstant);
    }
}
