package edu.shmonin.university.controller;

import edu.shmonin.university.model.Audience;
import edu.shmonin.university.service.AudienceService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/audiences")
public class AudiencesController {

    private final AudienceService audienceService;

    public AudiencesController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping()
    public String getAllAudiences(Model model) {
        model.addAttribute("audiences", audienceService.getAll());
        return "audiences/index";
    }

    @GetMapping()
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("audiences", audienceService.getAll(pageable));
        return "audiences/index";
    }

    @GetMapping("/{id}")
    public String getAudience(@PathVariable("id") int id, Model model) {
        model.addAttribute("audience", audienceService.get(id));
        return "audiences/show";
    }

    @GetMapping("/new")
    public String createNewAudience(@ModelAttribute("audience") Audience audience) {
        return "audiences/new";
    }

    @PostMapping("/new")
    public String createAudience(@ModelAttribute("audience") Audience audience) {
        audienceService.create(audience);
        return "redirect:/audiences";
    }

    @GetMapping("/{id}/edit")
    public String editAudience(Model model, @PathVariable("id") int id) {
        model.addAttribute("audience", audienceService.get(id));
        return "audiences/edit";
    }

    @PatchMapping("/{id}")
    public String updateAudience(@ModelAttribute("audience") Audience audience, @PathVariable("id") int id) {
        audience.setId(id);
        audienceService.update(audience);
        return "redirect:/audiences";
    }

    @DeleteMapping("/{id}")
    public String deleteAudience(@PathVariable("id") int id) {
        audienceService.delete(id);
        return "redirect:/audiences";
    }
}