package edu.shmonin.university.controller;

import edu.shmonin.university.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("students")
    public String viewStudents(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "students";
    }
}
