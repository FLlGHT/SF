package com.flight.sf.common;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MonthsDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;
    private int count;
    private List<TaskDTO> tasks;
    private List<StatsDTO> stats;

    public MonthsDTO() {
    }

    public MonthsDTO(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
        this.count = (int) ChronoUnit.MONTHS.between(from, to) + 1;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<StatsDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatsDTO> stats) {
        this.stats = stats;
    }
}
