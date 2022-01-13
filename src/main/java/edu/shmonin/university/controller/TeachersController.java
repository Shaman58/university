package edu.shmonin.university.controller;

import edu.shmonin.university.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeachersController {

    private final TeacherService teacherService;

    public TeachersController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("teachers")
    public String viewTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "teachers";
    }
}