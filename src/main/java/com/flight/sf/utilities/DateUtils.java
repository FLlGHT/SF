package com.flight.sf.utilities;

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
}
