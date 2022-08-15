package com.flight.sf.common;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CategoryDTO {

    private String name;
    private Map<String, TaskDTO> tasks = new HashMap<>();
    private String time;

    private long millis;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public String getTime() {
       long totalMillis = tasks.values().stream().map(TaskDTO::getMillis).reduce(Long::sum).orElse(0L);

        long hours = TimeUnit.MILLISECONDS.toHours(totalMillis);
        totalMillis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalMillis);

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
