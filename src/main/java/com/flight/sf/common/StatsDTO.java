package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FLIGHT
 * @creationDate 16.08.2022
 */
public class StatsDTO extends ProductivityDTO {

    private String totalProductiveTime;
    private String totalTime;
    private String totalPercentage;
    private int weeksNumber;
    private Map<Integer, Long> productiveTimeByWeek;

    private Map<Integer, Long> totalTimeByWeek;


    public StatsDTO() {
        initWeeksMap();
    }

    public StatsDTO(long productiveTime, long totalTime, double percentage, int weeksNumber) {
        this.totalProductiveTime = DateUtils.millisToDate(productiveTime);
        this.totalTime = DateUtils.millisToDate(totalTime);
        this.totalPercentage = String.format("%,.2f", percentage * 100);
        this.weeksNumber = weeksNumber;
    }

    private void initWeeksMap() {
        this.productiveTimeByWeek = new LinkedHashMap<>();
        this.totalTimeByWeek = new LinkedHashMap<>();

        initMap(productiveTimeByWeek);
        initMap(totalTimeByWeek);
        fillTotalTimeMap();
    }

    private void fillTotalTimeMap() {
        for (int i = 1; i <= totalTimeByWeek.size(); ++i) {
            totalTimeByWeek.put(i, DateUtils.weekLength(i));
        }
    }

    public String getTotalProductiveTime() {
        return totalProductiveTime;
    }

    public void setTotalProductiveTime(String totalProductiveTime) {
        this.totalProductiveTime = totalProductiveTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(String totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public int getWeeksNumber() {
        return weeksNumber;
    }

    public void setWeeksNumber(int weeksNumber) {
        this.weeksNumber = weeksNumber;
    }

    public Map<Integer, Long> getProductiveTimeByWeek() {
        return productiveTimeByWeek;
    }

    public void setProductiveTimeByWeek(Map<Integer, Long> productiveTimeByWeek) {
        this.productiveTimeByWeek = productiveTimeByWeek;
    }

    public void setPercentageByWeek(Map<Integer, Double> percentageByWeek) {
    }

    public List<String> getTimeByWeek() {
        return productiveTimeByWeek.values().stream().map(DateUtils::millisToDate).collect(Collectors.toList());
    }

    public List<String> getPercentageByWeek() {
        List<String> percentageByWeek = new ArrayList<>();
        for (int i = 1; i <= totalTimeByWeek.size(); ++i) {
            double percentage = productiveTimeByWeek.get(i) / (totalTimeByWeek.get(i) * 0.66);
            percentageByWeek.add(String.format("%,.2f", percentage * 100));
        }

        return percentageByWeek;
    }

}
