package com.flight.sf.service;

import com.flight.sf.common.PeriodDTO;
import com.flight.sf.common.StatsDTO;
import com.flight.sf.common.SummaryDTO;
import com.flight.sf.common.TaskDTO;
import com.flight.sf.utilities.DateUtils;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductivityService {

    @Autowired
    private CalendarService calendarService;

    public PeriodDTO getMonthlyProductivity(LocalDate from, LocalDate to) throws IOException {
        List<Event> events = calendarService.getEvents(from, to);
        PeriodDTO periodDTO = new PeriodDTO(ChronoUnit.MONTHS, from, to);
        Map<String, TaskDTO> tasks = new HashMap<>();
        Map<Integer, StatsDTO> stats = new HashMap<>();

        for (Event event : events) {
            long eventDuration = eventDuration(event);
            int monthNumber = DateUtils.monthNumber(event.getEnd().getDateTime().getValue());

            tasks.computeIfAbsent(event.getSummary().toLowerCase(), createTaskDTO(event, ChronoUnit.MONTHS, from, to)).addTaskMillis(monthNumber, eventDuration);
            stats.computeIfAbsent(monthNumber, createMonthStatsDTO(from, to, monthNumber)).addTaskMillis(eventDuration);
        }

        periodDTO.setTasks(tasks.values().stream().sorted(Comparator.comparing(TaskDTO::getTaskMillis).reversed()).collect(Collectors.toList()));
        periodDTO.setSummary(new SummaryDTO(new ArrayList<>(stats.values())));

        return periodDTO;
    }

    private long eventDuration(Event event) {
        return event.getEnd().getDateTime().getValue() - event.getStart().getDateTime().getValue();
    }

    private Function<String, TaskDTO> createTaskDTO(Event event, ChronoUnit chronoUnit, LocalDate from, LocalDate to) {
        return parameter -> new TaskDTO(event, chronoUnit, from, to);
    }

    private Function<Integer, StatsDTO> createMonthStatsDTO(LocalDate from, LocalDate to, int monthNumber) {
        YearMonth yearMonth = YearMonth.of(to.getYear(), monthNumber);

        LocalDate dateBegin = from.getMonthValue() == monthNumber ? from : from.withMonth(monthNumber).withDayOfMonth(1);
        LocalDate dateEnd = to.isBefore(yearMonth.atEndOfMonth()) ? to : yearMonth.atEndOfMonth();

        return parameter -> new StatsDTO(dateBegin, dateEnd);
    }
}
