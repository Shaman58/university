package edu.shmonin.university.controller;

import edu.shmonin.university.service.LectureService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/teacher/{teacherId}")
    public String getTeacherLecturesPage(Model model, Pageable pageable,
                                         @PathVariable int teacherId,
                                         @RequestParam LocalDate startDate,
                                         @RequestParam LocalDate endDate) {
        model.addAttribute("page", lectureService.getByTeacherIdAndPeriod(pageable, teacherId, startDate, endDate));
        model.addAttribute("teacherId", teacherId);
        return "lectures/teacher-lectures";
    }

    @GetMapping("/group/{groupId}")
    public String getGroupLecturesPage(Model model, Pageable pageable,
                                       @PathVariable int groupId,
                                       @RequestParam LocalDate startDate,
                                       @RequestParam LocalDate endDate) {
        model.addAttribute("page", lectureService.getByGroupIdAndPeriod(pageable, groupId, startDate, endDate));
        model.addAttribute("teacherId", groupId);
        return "lectures/group-lectures";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("lecture", lectureService.get(id));
        return "lectures/lecture";
    }
}