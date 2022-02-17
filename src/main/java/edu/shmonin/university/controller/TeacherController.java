package edu.shmonin.university.controller;

import edu.shmonin.university.service.TeacherService;
import edu.shmonin.university.service.VacationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final VacationService vacationService;

    public TeacherController(TeacherService teacherService, VacationService vacationService) {
        this.teacherService = teacherService;
        this.vacationService = vacationService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", teacherService.getAll(pageable));
        return "teachers/index";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("teacher", teacherService.get(id));
        model.addAttribute("vacations", vacationService.getByTeacherIdAndAcademicYear(id));
        return "teachers/teacher";
    }
}