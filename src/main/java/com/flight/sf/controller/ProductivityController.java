package com.flight.sf.controller;

import com.flight.sf.common.PeriodDTO;
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
import java.time.temporal.WeekFields;
import java.util.Locale;

@Controller
@RequestMapping("/productivity")
public class ProductivityController {

    @Autowired
    private ProductivityService productivityService;

    @GetMapping("/day")
    public String productivityByDay(Model model,
                                    @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                    @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to)
    throws IOException {

        if (from == null)
            from = LocalDate.now().minusDays(4);
        if (to == null)
            to = LocalDate.now();

        PeriodDTO days = productivityService.getDailyProductivity(from, to);
        model.addAttribute("days", days);
        return "day";
    }

    @GetMapping("/week")
    public String productivityByWeek(Model model,
                                     @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                     @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to)
            throws IOException {

        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        if (from == null)
            from = LocalDate.now().minusWeeks(4).with(weekFields.dayOfWeek(), 1);
        if (to == null)
            to = LocalDate.now();

        PeriodDTO weeks = productivityService.getWeeklyProductivity(from, to);
        model.addAttribute("weeks", weeks);
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
