package edu.shmonin.university.controller;

import edu.shmonin.university.service.LectureService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/teacher/{teacherId}")
    public String getTeacherLecturesPage(Model model, Pageable pageable, @PathVariable int teacherId) {
        model.addAttribute("page", lectureService.getByTeacherIdAndAcademicYear(pageable, teacherId));
        model.addAttribute("teacherId", teacherId);
        return "lectures/teacher-lectures";
    }

    @GetMapping("/group/{groupId}")
    public String getGroupLecturesPage(Model model, Pageable pageable, @PathVariable int groupId) {
        model.addAttribute("page", lectureService.getByGroupIdAndAcademicYear(pageable, groupId));
        model.addAttribute("teacherId", groupId);
        return "lectures/group-lectures";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("lecture", lectureService.get(id));
        return "lectures/lecture";
    }
}