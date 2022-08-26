package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;
import com.google.api.services.calendar.model.Event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskDTO  {

    private String name;
    private String category;
    private long taskMillis;
    private Map<Integer, Long> millisByPeriod;
    private int categoryCount;

    private ChronoUnit chronoUnit;

    public TaskDTO() {

    }

    public TaskDTO(Event event, ChronoUnit chronoUnit, LocalDate from, LocalDate to) {
        this.categoryCount = (int) chronoUnit.between(from, to) + 1;

        if (chronoUnit.equals(ChronoUnit.MONTHS))
            initMonthsPeriodMap(from, to);
        else if (chronoUnit.equals(ChronoUnit.WEEKS))
            initWeeksPeriodMap(from, to);

        this.name = event.getSummary();
        this.category = CategoryColor.getCategoryNameById(event.getColorId());
    }

    private void initMonthsPeriodMap(LocalDate from, LocalDate to) {
        this.millisByPeriod = new LinkedHashMap<>();
        for (int monthValue = from.getMonthValue(); monthValue <= to.getMonthValue(); ++monthValue) {
            millisByPeriod.put(monthValue, 0L);
        }
    }

    public void initWeeksPeriodMap(LocalDate from, LocalDate to) {
        this.millisByPeriod = new LinkedHashMap<>();
        for (int week = DateUtils.weekNumber(from); week <= DateUtils.weekNumber(to); ++week) {
            millisByPeriod.put(week, 0L);
        }
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

    public void addTaskMillis(int periodNumber, long eventDuration) {
        setTaskMillis(this.taskMillis + eventDuration);

        millisByPeriod.put(periodNumber, millisByPeriod.get(periodNumber) + eventDuration);
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

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    public ChronoUnit getChronoUnit() {
        return chronoUnit;
    }

    public void setChronoUnit(ChronoUnit chronoUnit) {
        this.chronoUnit = chronoUnit;
    }
}
