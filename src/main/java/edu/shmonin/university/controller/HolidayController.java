package edu.shmonin.university.controller;

import edu.shmonin.university.service.HolidayService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", holidayService.getAll(pageable));
        return "holidays/all";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("holiday", holidayService.get(id));
        return "holidays/holiday";
    }
}