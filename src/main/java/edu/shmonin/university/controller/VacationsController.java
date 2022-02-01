package edu.shmonin.university.controller;

import edu.shmonin.university.model.Vacation;
import edu.shmonin.university.service.TeacherService;
import edu.shmonin.university.service.VacationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/teachers/{teacherId}/vacations")
public class VacationsController {

    private final VacationService vacationService;
    private final TeacherService teacherService;

    public VacationsController(VacationService vacationService, TeacherService teacherService) {
        this.vacationService = vacationService;
        this.teacherService = teacherService;
    }

    @GetMapping()
    public String getPages(Model model, Pageable pageable, @PathVariable("teacherId") int teacherId) {
        var vacationPage = vacationService.getByTeacherSortedPagination(pageable, teacherId);
        model.addAttribute("vacationPage", vacationPage);
        model.addAttribute("teacherId", teacherId);
        var totalPages = vacationPage.getTotalPages();
        if (totalPages > 1) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "vacations/index";
    }

    @GetMapping("/new")
    public String createNew(@PathVariable("teacherId") int teacherId, Model model, @ModelAttribute("vacation") Vacation vacation) {
        model.addAttribute("allVacations", vacationService.getAll());
        return "vacations/new";
    }

    @PostMapping("/new")
    public String create(@PathVariable("teacherId") int teacherId, @ModelAttribute("vacation") Vacation vacation) {
        vacation.setTeacher(teacherService.get(teacherId));
        vacationService.create(vacation);
        return "redirect:/teachers/{teacherId}/vacations";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id, @PathVariable("teacherId") int teacherId) {
        model.addAttribute("vacation", vacationService.get(id));
        model.addAttribute("teacherId", teacherId);
        return "vacations/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("teacherId") int teacherId, @ModelAttribute("vacation") Vacation vacation,
                         @PathVariable("id") int id) {
        vacation.setTeacher(teacherService.get(teacherId));
        vacationService.update(vacation);
        return "redirect:/teachers/{teacherId}/vacations";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("teacherId") int teacherId, @PathVariable("id") int id) {
        vacationService.delete(id);
        return "redirect:/teachers/{teacherId}/vacations";
    }
}
