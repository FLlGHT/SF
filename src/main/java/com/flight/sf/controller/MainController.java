package com.flight.sf.controller;

import com.flight.sf.common.CategoryDTO;
import com.flight.sf.common.EventDTO;
import com.flight.sf.service.CalendarService;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
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

    @GetMapping("/next-events")
    public String getUpcomingEvents(Model model) throws GeneralSecurityException, IOException {
        List<EventDTO> events = calendarService.getNextEvents(5);
        model.addAttribute("events", events);
        return "next-events";
    }

    @GetMapping("/this-month")
    public String thisMonthEvents(Model model) throws GeneralSecurityException, IOException {
        List<EventDTO> events = calendarService.getLastMonthEvents();
        model.addAttribute("events", events);
        return "this-month";
    }

    @GetMapping("/productivity")
    public String lastMonthProductivity(Model model) throws IOException, ParseException {
        List<CategoryDTO> categories = calendarService.getLastMonthProductivity();
        model.addAttribute("categories", categories);
        return "productivity";
    }
}
