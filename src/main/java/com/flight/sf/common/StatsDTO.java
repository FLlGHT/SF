package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author FLIGHT
 * @creationDate 16.08.2022
 */
public class StatsDTO {

    private long taskMillis;

    private long totalMillis;

    public StatsDTO() {

    }

    public StatsDTO(LocalDate from, LocalDate to) {
        LocalDateTime beginDate = from.atStartOfDay();
        LocalDateTime endDate = to.plusDays(1).atStartOfDay();
        this.totalMillis = ChronoUnit.MILLIS.between(beginDate, endDate.isBefore(LocalDateTime.now()) ? endDate : LocalDateTime.now());
    }

    public StatsDTO(long taskMillis, long totalMillis) {
        this.taskMillis = taskMillis;
        this.totalMillis = totalMillis;
    }


    public String getProductiveTime() {
        return DateUtils.millisToDate(taskMillis);
    }

    public String getTotalTime() {
        return DateUtils.millisToDate(getTotalMillis());
    }

    public long getTotalMillis() {
        return totalMillis;
    }

    public String getPercentage() {
        return String.format("%,.2f", (getTaskMillis() / (getTotalMillis() * 0.66)) * 100);
    }

    public long getTaskMillis() {
        return taskMillis;
    }

    public void setTaskMillis(long taskMillis) {
        this.taskMillis = taskMillis;
    }

    public void addTaskMillis(long taskMillis) {
        setTaskMillis(this.taskMillis + taskMillis);
    }
}
