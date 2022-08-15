package com.flight.sf.common;

import java.util.concurrent.TimeUnit;

public class TaskDTO {

    private String name;
    private String time;

    private long millis;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        long interval = millis;

        long hours = TimeUnit.MILLISECONDS.toHours(interval);
        interval -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(interval);

        String stringInterval = "%02d:%02d";
        return String.format(stringInterval, hours, minutes);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }
}
