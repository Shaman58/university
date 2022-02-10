package edu.shmonin.university.controller;

import edu.shmonin.university.service.GroupService;
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
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getPages(Model model, Pageable pageable) {
        model.addAttribute("page", studentService.getAll(pageable));
        return "students/index";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable("id") int id) {
        model.addAttribute("student", studentService.get(id));
        model.addAttribute("groups", groupService.getAll());
        return "students/student";
    }
}