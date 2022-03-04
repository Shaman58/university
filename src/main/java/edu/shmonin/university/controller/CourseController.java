package edu.shmonin.university.controller;

import edu.shmonin.university.model.Course;
import edu.shmonin.university.service.CourseService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable) {
        model.addAttribute("page", courseService.getAll(pageable));
        return "courses/all";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("course", courseService.get(id));
        return "courses/course";
    }

    @GetMapping("/new")
    public String createNew(@ModelAttribute("course") Course course) {
        return "courses/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("course") Course course) {
        courseService.create(course);
        return "redirect:/courses";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("course", courseService.get(id));
        return "courses/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("course") Course course, @PathVariable("id") int id) {
        course.setId(id);
        courseService.update(course);
        return "redirect:/courses";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        courseService.delete(id);
        return "redirect:/courses";
    }
}