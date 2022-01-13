package edu.shmonin.university.controller;

import edu.shmonin.university.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final StudentService studentService;

    public MainController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String first(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "main";
    }
}