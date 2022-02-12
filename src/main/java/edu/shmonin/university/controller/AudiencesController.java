package edu.shmonin.university.controller;

import edu.shmonin.university.service.AudienceService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/audiences")
public class AudiencesController {

    private final AudienceService audienceService;

    public AudiencesController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", audienceService.getAll(pageable));
        return "audiences/index";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable("id") int id) {
        model.addAttribute("audience", audienceService.get(id));
        return "audiences/audience";
    }
}