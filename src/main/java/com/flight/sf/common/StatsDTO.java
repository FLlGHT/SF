package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;

/**
 * @author FLIGHT
 * @creationDate 16.08.2022
 */
public class StatsDTO extends ProductivityDTO {

    private long taskMillis;
    private long totalMillis;
    private String productiveTime;
    private String totalTime;
    private String percentage;


    public StatsDTO() {

    }

    public StatsDTO(long productiveTime, long totalTime, double percentage) {
        this.productiveTime = DateUtils.millisToDate(productiveTime);
        this.totalTime = DateUtils.millisToDate(totalTime);
        this.percentage = String.format("%,.2f", percentage * 100);
    }


    public String getProductiveTime() {
        return productiveTime;
    }

    public void setProductiveTime(String productiveTime) {
        this.productiveTime = productiveTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
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

    public long getTotalMillis() {
        return totalMillis;
    }

    public void setTotalMillis(long totalMillis) {
        this.totalMillis = totalMillis;
    }
}
