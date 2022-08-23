package com.flight.sf.controller;

import com.flight.sf.common.MonthsDTO;
import com.flight.sf.common.WeeksDTO;
import com.flight.sf.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/productivity")
public class ProductivityController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/week")
    public String productivityByWeek(Model model,
                                     @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                     @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to)
            throws IOException {
        calendarService.getProductivityByWeek(model);

        model.addAttribute("weeks", new WeeksDTO());
        return "week";
    }

    @GetMapping("/month")
    public String productivityByMonth(Model model,
                                      @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to)
            throws IOException {

        model.addAttribute("months", new MonthsDTO());
        return "month";
    }
}
