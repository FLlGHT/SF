package com.flight.sf.controller;

import com.flight.sf.common.StatsDTO;
import com.flight.sf.common.TaskDTO;
import com.flight.sf.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * @author FLIGHT
 * @creationDate 13.08.2022
 */
@Controller
public class MainController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/")
    public String mainPage() {
        return "main-page";
    }

    @GetMapping("/productivity-table")
    public String productivityTable(Model model) throws IOException {
        calendarService.getMonthTasks(model);
//        StatsDTO stats = calendarService.getTotalStats(tasks);

        return "productivity";
    }
}
