package edu.shmonin.university.controller;

import edu.shmonin.university.model.Duration;
import edu.shmonin.university.service.DurationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/durations")
public class DurationController {

    private final DurationService durationService;

    public DurationController(DurationService durationService) {
        this.durationService = durationService;
    }

    @GetMapping
    public String getPages(Model model, Pageable pageable) {
        model.addAttribute("page", durationService.getAll(pageable));
        return "durations/index";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable("id") int id) {
        model.addAttribute("duration", durationService.get(id));
        return "durations/duration";
    }
}
