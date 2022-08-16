package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;

import java.util.concurrent.TimeUnit;

public class TaskDTO {

    private String name;
    private String time;
    private long millis;

    private String category;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return DateUtils.millisToDate(millis);
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

    public void addMillis(long millis) {
        setMillis(this.millis + millis);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
