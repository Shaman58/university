package edu.shmonin.university.controller;

import edu.shmonin.university.model.Holiday;
import edu.shmonin.university.service.HolidayService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/new")
    public String createNew(@ModelAttribute("holiday") Holiday holiday) {
        return "holidays/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("holiday") Holiday holiday) {
        holidayService.create(holiday);
        return "redirect:/holidays";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("holiday", holidayService.get(id));
        return "holidays/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("holiday") Holiday holiday,
                         @PathVariable("id") int id) {
        holiday.setId(id);
        holidayService.update(holiday);
        return "redirect:/holidays";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        holidayService.delete(id);
        return "redirect:/holidays";
    }
}