package edu.shmonin.university.controller;

import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.service.CourseService;
import edu.shmonin.university.service.TeacherService;
import edu.shmonin.university.service.VacationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final VacationService vacationService;
    private final CourseService courseService;

    public TeacherController(TeacherService teacherService, VacationService vacationService, CourseService courseService) {
        this.teacherService = teacherService;
        this.vacationService = vacationService;
        this.courseService = courseService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", teacherService.getAll(pageable));
        return "teachers/all";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("teacher", teacherService.get(id));
        model.addAttribute("vacations", vacationService.getByTeacherIdAndAcademicYear(id));
        return "teachers/teacher";
    }

    @GetMapping("/new")
    public String createNew(Model model, @ModelAttribute("teacher") Teacher teacher) {
        model.addAttribute("allCourses", courseService.getAll());
        return "teachers/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("allCourses", courseService.getAll());
        model.addAttribute("teacher", teacherService.get(id));
        return "teachers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("teacher") Teacher teacher,
                         @PathVariable("id") int id) {
        teacher.setId(id);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }
}