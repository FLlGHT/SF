package com.flight.sf.utilities;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author FLIGHT
 * @creationDate 16.08.2022
 */
public class DateUtils {

    public static String millisToDate(long millis) {
        long interval = millis;

        long hours = TimeUnit.MILLISECONDS.toHours(interval);
        interval -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(interval);

        String stringInterval = "%02d:%02d";
        return String.format(stringInterval, hours, minutes);
    }

    public static int weekNumber(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());

        return localDate.get(WeekFields.of(Locale.getDefault()).weekOfMonth());
    }


    public static long weekLength(int weekNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(getFirstDayOfWeek(weekNumber).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return TimeUnit.DAYS.toMillis(calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
    }

    static LocalDate getFirstDayOfWeek(int weekNumber) {
        return LocalDate
                .now()
               // .withMonth(month)
                .with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek())
                .with(WeekFields.of(Locale.getDefault()).weekOfMonth(), weekNumber);
    }
}
