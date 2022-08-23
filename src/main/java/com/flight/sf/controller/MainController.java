package com.flight.sf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author FLIGHT
 * @creationDate 13.08.2022
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "main-page";
    }
}
