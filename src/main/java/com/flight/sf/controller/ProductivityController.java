package com.flight.sf.controller;

import com.flight.sf.common.PeriodDTO;
import com.flight.sf.service.CalendarService;
import com.flight.sf.service.ProductivityService;
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

    @Autowired
    private ProductivityService productivityService;

    @GetMapping("/day")
    public String productivityByDay(Model model,
                                    @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                    @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to)
    throws IOException {

        return "day";
    }

    @GetMapping("/week")
    public String productivityByWeek(Model model,
                                     @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                     @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to)
            throws IOException {

//        productivityService.getWeeklyProductivity(model, from, to);

        model.addAttribute("weeks", new PeriodDTO());
        return "week";
    }

    @GetMapping("/month")
    public String productivityByMonth(Model model,
                                      @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to)
            throws IOException {

        if (from == null)
            from = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        if (to == null)
            to = LocalDate.now();

        PeriodDTO months = productivityService.getMonthlyProductivity(from, to);
        model.addAttribute("months", months);
        return "month";
    }
}
