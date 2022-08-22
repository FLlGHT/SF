package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;

import java.time.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskDTO extends ProductivityDTO {

    private String name;
    private long millis;
    private Map<Integer, Long> millisByWeek;

    private String category;

    public TaskDTO() {
        initWeeksMap();
    }

    private void initWeeksMap() {
        this.millisByWeek = new LinkedHashMap<>();
        initMap(millisByWeek);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalTime() {
        return DateUtils.millisToDate(millis);
    }

    public List<String> getTimeByWeek() {
        return millisByWeek.values().stream().map(DateUtils::millisToDate).collect(Collectors.toList());
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

    public Map<Integer, Long> getMillisByWeek() {
        return millisByWeek;
    }

    public void setMillisByWeek(Map<Integer, Long> millisByWeek) {
        this.millisByWeek = millisByWeek;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
