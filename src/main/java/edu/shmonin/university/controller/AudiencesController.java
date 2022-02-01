package edu.shmonin.university.controller;

import edu.shmonin.university.model.Audience;
import edu.shmonin.university.service.AudienceService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/audiences")
public class AudiencesController {

    private final AudienceService audienceService;

    public AudiencesController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping()
    public String getPages(Model model, Pageable pageable) {
        var audiencePages = audienceService.getSortedPaginated(pageable);
        model.addAttribute("audiencePage", audiencePages);
        var totalPages = audiencePages.getTotalPages();
        if (totalPages > 1) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "audiences/index";
    }

    @GetMapping("/new")
    public String createNew(@ModelAttribute("audience") Audience audience) {
        return "audiences/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("audience") Audience audience) {
        audienceService.create(audience);
        return "redirect:/audiences";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("audience", audienceService.get(id));
        return "audiences/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("audience") Audience audience, @PathVariable("id") int id) {
        audience.setId(id);
        audienceService.update(audience);
        return "redirect:/audiences";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        audienceService.delete(id);
        return "redirect:/audiences";
    }
}