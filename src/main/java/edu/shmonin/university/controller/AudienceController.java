package edu.shmonin.university.controller;

import edu.shmonin.university.model.Audience;
import edu.shmonin.university.service.AudienceService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/audiences")
public class AudienceController {

    private final AudienceService audienceService;

    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", audienceService.getAll(pageable));
        return "audiences/all";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("audience", audienceService.get(id));
        return "audiences/audience";
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