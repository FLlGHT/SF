package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;
import com.google.api.services.calendar.model.Event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskDTO extends ProductivityDTO {

    private String name;
    private String category;
    private long taskMillis;
    private Map<Integer, Long> millisByPeriod;
    private int categoryCount;

    public TaskDTO() {
        initPeriodMap();
    }

    public TaskDTO(Event event, LocalDate from, LocalDate to) {
        this.categoryCount = (int) ChronoUnit.MONTHS.between(from, to) + 1;

        this.millisByPeriod = new LinkedHashMap<>();
        for (int i = from.getMonthValue(); i <= to.getMonthValue(); ++i) {
            millisByPeriod.put(i, 0L);
        }

        this.name = event.getSummary();
        this.category = CategoryColor.getCategoryNameById(event.getColorId());
    }

    private void initPeriodMap() {
        this.millisByPeriod = new LinkedHashMap<>();
        initMap(millisByPeriod);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalTime() {
        return DateUtils.millisToDate(taskMillis);
    }

    public List<String> getTimeByPeriod() {
        return millisByPeriod.values().stream().map(DateUtils::millisToDate).collect(Collectors.toList());
    }

    public void setTime(String time) {
    }

    public long getTaskMillis() {
        return taskMillis;
    }

    public void setTaskMillis(long taskMillis) {
        this.taskMillis = taskMillis;
    }

    public void addTaskMillis(long millis) {
        setTaskMillis(this.taskMillis + millis);
    }

    public Map<Integer, Long> getMillisByPeriod() {
        return millisByPeriod;
    }

    public void setMillisByPeriod(Map<Integer, Long> millisByPeriod) {
        this.millisByPeriod = millisByPeriod;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
