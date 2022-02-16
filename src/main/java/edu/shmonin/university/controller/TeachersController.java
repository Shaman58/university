package edu.shmonin.university.controller;

import edu.shmonin.university.service.TeacherService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

    private final TeacherService teacherService;

    public TeachersController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String getPages(Model model, Pageable pageable) {
        model.addAttribute("page", teacherService.getAll(pageable));
        return "teachers/index";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("teacher", teacherService.get(id));
        return "teachers/teacher";
    }
}