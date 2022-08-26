package com.flight.sf.common;

import java.util.Map;

public class SummaryDTO {
    private final Map<String, StatsDTO> periodStats;
    private final StatsDTO totalStats;

    public SummaryDTO(Map<String, StatsDTO> periodStats) {
        this.periodStats = periodStats;
        this.totalStats = createTotalStats();
    }

    private StatsDTO createTotalStats() {
        long taskTime = 0, totalTime = 0;
        for (StatsDTO periodStat : periodStats.values()) {
            taskTime += periodStat.getTaskMillis();
            totalTime += periodStat.getTotalMillis();
        }
        return new StatsDTO(taskTime, totalTime);
    }

    public Map<String, StatsDTO> getPeriodStats() {
        return periodStats;
    }

    public StatsDTO getTotalStats() {
        return totalStats;
    }
}
