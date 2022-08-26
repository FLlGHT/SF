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
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductivityService {

    @Autowired
    private CalendarService calendarService;

    public PeriodDTO getDailyProductivity(LocalDate from, LocalDate to) throws IOException {
        List<Event> events = calendarService.getEvents(from, to);
        PeriodDTO periodDTO = new PeriodDTO(ChronoUnit.DAYS, from, to);
        Map<String, TaskDTO> tasks = new HashMap<>();
        Map<Integer, StatsDTO> stats = new HashMap<>();

        for (Event event : events) {
            long eventDuration = eventDuration(event);
            int dayNumber = DateUtils.dayNumber(event.getEnd().getDateTime().getValue());

            tasks.computeIfAbsent(event.getSummary().toLowerCase(), createTaskDTO(event, ChronoUnit.DAYS, from, to))
                    .addTaskMillis(dayNumber, eventDuration);
            stats.computeIfAbsent(dayNumber,  createDayStatsDTO(from, to, dayNumber))
                    .addTaskMillis(eventDuration);
        }

        periodDTO.setTasks(createTasks(tasks));
        periodDTO.setSummary(createDaySummary(stats, from, to));

        return periodDTO;
    }
    public PeriodDTO getWeeklyProductivity(LocalDate from, LocalDate to) throws IOException {
        List<Event> events = calendarService.getEvents(from, to);
        PeriodDTO periodDTO = new PeriodDTO(ChronoUnit.WEEKS, from, to);
        Map<String, TaskDTO> tasks = new HashMap<>();
        Map<Integer, StatsDTO> stats = new HashMap<>();

        for (Event event : events) {
            long eventDuration = eventDuration(event);
            int weekNumber = DateUtils.weekNumber(event.getEnd().getDateTime().getValue());

            tasks.computeIfAbsent(event.getSummary().toLowerCase(), createTaskDTO(event, ChronoUnit.WEEKS, from, to))
                 .addTaskMillis(weekNumber, eventDuration);
            stats.computeIfAbsent(weekNumber, createWeekStatsDTO(from, to, weekNumber))
                 .addTaskMillis(eventDuration);
        }

        periodDTO.setTasks(createTasks(tasks));
        periodDTO.setSummary(createWeekSummary(stats, from, to));

        return periodDTO;
    }

    public PeriodDTO getMonthlyProductivity(LocalDate from, LocalDate to) throws IOException {
        List<Event> events = calendarService.getEvents(from, to);
        PeriodDTO periodDTO = new PeriodDTO(ChronoUnit.MONTHS, from, to);
        Map<String, TaskDTO> tasks = new HashMap<>();
        Map<Month, StatsDTO> stats = new HashMap<>();

        for (Event event : events) {
            long eventDuration = eventDuration(event);
            int monthNumber = DateUtils.monthNumber(event.getEnd().getDateTime().getValue());

            tasks.computeIfAbsent(event.getSummary().toLowerCase(), createTaskDTO(event, ChronoUnit.MONTHS, from, to))
                 .addTaskMillis(monthNumber, eventDuration);
            stats.computeIfAbsent(Month.of(monthNumber), createMonthStatsDTO(from, to, monthNumber))
                 .addTaskMillis(eventDuration);
        }

        periodDTO.setTasks(createTasks(tasks));
        periodDTO.setSummary(createMonthSummary(stats, from, to));

        return periodDTO;
    }

    private List<TaskDTO> createTasks(Map<String, TaskDTO> tasks) {
        return tasks.values()
                    .stream()
                    .sorted(Comparator.comparing(TaskDTO::getTaskMillis).reversed())
                    .collect(Collectors.toList());
    }

    private SummaryDTO createDaySummary(Map<Integer, StatsDTO> stats, LocalDate from, LocalDate to) {
        if (stats.size() < ChronoUnit.DAYS.between(from, to) + 1) {
            for (int dayNumber = DateUtils.dayNumber(from); dayNumber <= DateUtils.dayNumber(to); ++dayNumber) {
                stats.computeIfAbsent(dayNumber, createDayStatsDTO(from, to, dayNumber));
            }
        }

        Function<Map.Entry<Integer, StatsDTO>, String> keyMapper = entry -> DateUtils.dateByDayOfYear(entry.getKey());

        return new SummaryDTO(stats.entrySet().stream()
                                   .sorted(
                                           Comparator.comparingInt(Map.Entry::getKey))
                                   .collect(Collectors.toMap(
                                           keyMapper, Map.Entry::getValue, (key, value) -> key, LinkedHashMap::new
                                   )));
    }

    private SummaryDTO createWeekSummary(Map<Integer, StatsDTO> stats, LocalDate from, LocalDate to) {
        if (stats.size() < ChronoUnit.WEEKS.between(from, to) + 1) {
            for (int weekNumber = DateUtils.weekNumber(from); weekNumber <= DateUtils.weekNumber(to); ++weekNumber) {
                stats.computeIfAbsent(weekNumber, createWeekStatsDTO(from, to, weekNumber));
            }
        }

        Function<Map.Entry<Integer, StatsDTO>, String> keyMapper = entry -> "Week " + entry.getKey();

        return new SummaryDTO(stats.entrySet().stream()
                                   .sorted(
                                           Comparator.comparingInt(Map.Entry::getKey))
                                   .collect(Collectors.toMap(
                                           keyMapper, Map.Entry::getValue, (key, value) -> key, LinkedHashMap::new
                                   )));
    }


    private SummaryDTO createMonthSummary(Map<Month, StatsDTO> stats, LocalDate from, LocalDate to) {
        if (stats.size() < ChronoUnit.MONTHS.between(from, to) + 1) {
            for (int monthNumber = from.getMonthValue(); monthNumber <= to.getMonthValue(); ++monthNumber) {
                stats.computeIfAbsent(Month.of(monthNumber), createMonthStatsDTO(from, to, monthNumber));
            }
        }

        Function<Map.Entry<Month, StatsDTO>, String> keyMapper =
                stats.size() > 4 ?
                entry -> entry.getKey().getValue() + "." + entry.getKey().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US) :
                entry -> entry.getKey().getDisplayName(TextStyle.FULL_STANDALONE, Locale.US);

        return new SummaryDTO(
                stats.entrySet().stream()
                     .sorted(Comparator.comparingInt(
                             entry -> entry.getKey().getValue()))
                     .collect(Collectors.toMap(
                             keyMapper, Map.Entry::getValue, (key, value) -> key, LinkedHashMap::new
                     )));
    }

    private long eventDuration(Event event) {
        return event.getEnd().getDateTime().getValue() - event.getStart().getDateTime().getValue();
    }

    private Function<String, TaskDTO> createTaskDTO(Event event, ChronoUnit chronoUnit, LocalDate from, LocalDate to) {
        return parameter -> new TaskDTO(event, chronoUnit, from, to);
    }

    private Function<Integer, StatsDTO> createDayStatsDTO(LocalDate from, LocalDate to, int dayNumber) {
        LocalDate date = LocalDate.ofYearDay(from.getYear(), dayNumber);

        return parameter -> new StatsDTO(date, date);
    }

    private Function<Integer, StatsDTO> createWeekStatsDTO(LocalDate from, LocalDate to, int weekNumber) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        LocalDate weekStart = from.with(weekFields.weekOfYear(), weekNumber).with(weekFields.dayOfWeek(), 1);
        LocalDate weekEnd = to.with(weekFields.weekOfYear(), weekNumber).with(weekFields.dayOfWeek(), 7);

        LocalDate dateBegin = from.isBefore(weekStart) ? weekStart : from;
        LocalDate dateEnd = to.isBefore(weekEnd) ? to : weekEnd;

        return parameter -> new StatsDTO(dateBegin, dateEnd);
    }

    private Function<Month, StatsDTO> createMonthStatsDTO(LocalDate from, LocalDate to, int monthNumber) {
        YearMonth yearMonth = YearMonth.of(to.getYear(), monthNumber);

        LocalDate dateBegin =
                from.getMonthValue() == monthNumber ? from : from.withMonth(monthNumber).withDayOfMonth(1);
        LocalDate dateEnd = to.isBefore(yearMonth.atEndOfMonth()) ? to : yearMonth.atEndOfMonth();

        return parameter -> new StatsDTO(dateBegin, dateEnd);
    }
}
