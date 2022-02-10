package edu.shmonin.university.controller;

import edu.shmonin.university.service.implementation.StudentServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final StudentServiceImpl studentService;

    public MainController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String first(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "main";
    }
}