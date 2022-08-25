package com.flight.sf.common;

import java.util.List;

public class SummaryDTO {
    private final List<StatsDTO> periodStats;
    private final StatsDTO totalStats;

    public SummaryDTO(List<StatsDTO> periodStats) {
        this.periodStats = periodStats;
        this.totalStats = createTotalStats();
    }

    private StatsDTO createTotalStats() {
        long taskTime = 0, totalTime = 0;
        for (StatsDTO periodStat : periodStats) {
            taskTime += periodStat.getTaskMillis();
            totalTime += periodStat.getTotalMillis();
        }
        return new StatsDTO(taskTime, totalTime);
    }

    public List<StatsDTO> getPeriodStats() {
        return periodStats;
    }

    public StatsDTO getTotalStats() {
        return totalStats;
    }
}
