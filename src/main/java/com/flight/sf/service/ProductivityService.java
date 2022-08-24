package com.flight.sf.service;

import com.flight.sf.common.CategoryColor;
import com.flight.sf.common.MonthsDTO;
import com.flight.sf.common.StatsDTO;
import com.flight.sf.common.TaskDTO;
import com.flight.sf.utilities.DateUtils;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductivityService {

    @Autowired
    private CalendarService calendarService;

    public MonthsDTO getMonthlyProductivity(LocalDate from, LocalDate to) throws IOException {
        List<Event> events = calendarService.getMonthEvents(from, to);
        MonthsDTO monthsDTO = new MonthsDTO(from, to);
        Map<String, TaskDTO> tasks = new HashMap<>();
        Map<Integer, StatsDTO> stats = new HashMap<>();

        for (Event event : events) {
            long eventDuration = eventDuration(event);
            int monthNumber = DateUtils.monthNumber(event.getEnd().getDateTime().getValue());
            TaskDTO task = tasks.computeIfAbsent(event.getSummary().toLowerCase(), parameter -> new TaskDTO(event, from, to));
            StatsDTO stat = stats.computeIfAbsent(monthNumber, parameter -> new StatsDTO());

            Map<Integer, Long> timeByMonth = task.getMillisByPeriod();
            timeByMonth.put(monthNumber, timeByMonth.get(monthNumber) + eventDuration);

            task.addTaskMillis(eventDuration);
            stat.addTaskMillis(eventDuration);
        }

        monthsDTO.setTasks(new ArrayList<>(tasks.values()));
        return monthsDTO;
    }


    public void getWeeklyProductivity(Model model, LocalDate from, LocalDate to) throws IOException {
        List<Event> events = calendarService.getMonthEvents(from, to);
        Map<String, TaskDTO> tasks = new HashMap<>();
        StatsDTO stats = new StatsDTO();

        long totalTime = ChronoUnit.MILLIS.between(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0), LocalDateTime.now());
        long totalProductiveTime = 0;
        int weeksNumber = 0;

        for (Event event : events) {
            TaskDTO task = tasks.computeIfAbsent(event.getSummary().toLowerCase(), param -> new TaskDTO());
            long eventDuration = eventDuration(event);
            int weekNumber = DateUtils.weekNumber(event.getEnd().getDateTime().getValue());
            weeksNumber = Math.max(weekNumber, weeksNumber);

            Map<Integer, Long> taskTimeByWeek = task.getMillisByPeriod();
            taskTimeByWeek.put(weekNumber, taskTimeByWeek.get(weekNumber) + eventDuration);

//            Map<Integer, Long> totalTimeByWeek = stats.getProductiveTimeByWeek();
//            totalTimeByWeek.put(weekNumber, totalTimeByWeek.get(weekNumber) + eventDuration);
            totalProductiveTime += eventDuration;

            task.setName(event.getSummary());
            task.addTaskMillis(eventDuration);
            task.setCategory(CategoryColor.getCategoryNameById(event.getColorId()));
        }

        stats.setProductiveTime(DateUtils.millisToDate(totalProductiveTime));
        stats.setTotalTime(DateUtils.millisToDate(totalTime));
        stats.setPercentage(String.format("%,.2f", totalProductiveTime / (totalTime * 0.66) * 100));
//        stats.setWeeksNumber(weeksNumber);

        addAttributes(model, new ArrayList<>(tasks.values()), stats);
    }

    private void addAttributes(Model model, List<TaskDTO> tasks, StatsDTO stats) {
        model.addAttribute("tasks", tasks);
        model.addAttribute("stats", stats);
    }

    private long eventDuration(Event event) {
        return event.getEnd().getDateTime().getValue() - event.getStart().getDateTime().getValue();
    }
}
