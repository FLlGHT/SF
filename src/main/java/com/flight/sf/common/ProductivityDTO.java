package com.flight.sf.common;

import com.flight.sf.utilities.DateUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Map;

public class ProductivityDTO {

    public void initMap(Map<Integer, Long> timeByPeriod) {
        YearMonth yearMonth = YearMonth.now();
        LocalDate date = yearMonth.atEndOfMonth();
        long dateTime = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        int maxWeek = DateUtils.weekNumber(dateTime);

        for (int i = 1; i <= maxWeek; ++i) {
            timeByPeriod.put(i, 0L);
        }
    }

}
