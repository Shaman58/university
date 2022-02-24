package edu.shmonin.university.controller;

import edu.shmonin.university.service.StudentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", studentService.getAll(pageable));
        return "students/all";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("student", studentService.get(id));
        return "students/student";
    }
}