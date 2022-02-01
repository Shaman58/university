package edu.shmonin.university.controller;

import edu.shmonin.university.model.Duration;
import edu.shmonin.university.service.DurationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/durations")
public class DurationController {

    private final DurationService durationService;

    public DurationController(DurationService durationService) {
        this.durationService = durationService;
    }

    @GetMapping()
    public String getPages(Model model, Pageable pageable) {
        var durationPages = durationService.getSortedPaginated(pageable);
        model.addAttribute("durationPage", durationPages);
        var totalPages = durationPages.getTotalPages();
        if (totalPages > 1) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "durations/index";
    }

    @GetMapping("/new")
    public String createNew(@ModelAttribute("duration") Duration duration) {
        return "durations/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("duration") Duration duration) {
        durationService.create(duration);
        return "redirect:/durations";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("duration", durationService.get(id));
        return "durations/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("duration") Duration duration, @PathVariable("id") int id) {
        duration.setId(id);
        durationService.update(duration);
        return "redirect:/durations";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        durationService.delete(id);
        return "redirect:/durations";
    }
}
