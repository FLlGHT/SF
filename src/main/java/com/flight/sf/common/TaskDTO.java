package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;

import java.util.HashMap;
import java.util.Map;

public class TaskDTO {

    private String name;
    private long millis;

    private Map<String, Long> weekMillis = new HashMap<>();
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

    public Map<String, Long> getWeekMillis() {
        return weekMillis;
    }

    public void setWeekMillis(Map<String, Long> weekMillis) {
        this.weekMillis = weekMillis;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
