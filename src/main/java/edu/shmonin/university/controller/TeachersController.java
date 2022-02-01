package edu.shmonin.university.controller;

import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.service.CourseService;
import edu.shmonin.university.service.TeacherService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

    private final TeacherService teacherService;
    private final CourseService courseService;

    public TeachersController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @GetMapping()
    public String getPages(Model model, Pageable pageable) {
        var teacherPages = teacherService.getSortedPaginated(pageable);
        model.addAttribute("teacherPage", teacherPages);
        var totalPages = teacherPages.getTotalPages();
        if (totalPages > 1) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "teachers/index";
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