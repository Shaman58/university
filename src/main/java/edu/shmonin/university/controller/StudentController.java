package edu.shmonin.university.controller;

import edu.shmonin.university.model.Student;
import edu.shmonin.university.service.GroupService;
import edu.shmonin.university.service.StudentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", studentService.getAll(pageable));
        return "students/all";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("student", studentService.get(id));
        return "students/student";
    }

    @GetMapping("/new")
    public String createNew(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "students/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("student") Student student) {
        studentService.create(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("student", studentService.get(id));
        model.addAttribute("groups", groupService.getAll());
        return "students/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") Student student,
                         @PathVariable("id") int id) {
        student.setId(id);
        studentService.update(student);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}