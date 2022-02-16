package edu.shmonin.university.controller;

import edu.shmonin.university.service.VacationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teachers/{teacherId}/vacations")
public class VacationsController {

    private final VacationService vacationService;

    public VacationsController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping
    public String getPages(Model model, Pageable pageable, @PathVariable int teacherId) {
        model.addAttribute("page", vacationService.getByTeacherId(pageable, teacherId));
        model.addAttribute("teacherId", teacherId);
        return "vacations/index";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id, @PathVariable int teacherId) {
        model.addAttribute("vacation", vacationService.get(id));
        model.addAttribute("teacherId", teacherId);
        return "vacations/vacation";
    }
}
