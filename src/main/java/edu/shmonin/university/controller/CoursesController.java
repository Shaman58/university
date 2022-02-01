package edu.shmonin.university.controller;

import edu.shmonin.university.model.Course;
import edu.shmonin.university.service.CourseService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final CourseService courseService;

    public CoursesController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping()
    public String getPages(Model model, Pageable pageable) {
        var coursesPages = courseService.getSortedPaginated(pageable);
        model.addAttribute("coursePage", coursesPages);
        var totalPages = coursesPages.getTotalPages();
        if (totalPages > 1) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "courses/index";
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