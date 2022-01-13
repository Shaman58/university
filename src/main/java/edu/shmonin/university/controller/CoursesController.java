package edu.shmonin.university.controller;


import edu.shmonin.university.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final CourseService courseService;

    public CoursesController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping()
    public String getAllCourses(Model model) {
        model.addAttribute("courses", courseService.getAll());
        return "courses/index";
    }

    @GetMapping("/{id}")
    public String getCourse(@PathVariable("id") int id, Model model) {
        model.addAttribute("course", courseService.get(id));
        return "courses/show";
    }
}
