package edu.shmonin.university.controller;

import edu.shmonin.university.model.Vacation;
import edu.shmonin.university.service.TeacherService;
import edu.shmonin.university.service.VacationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teachers/{teacherId}/vacations")
public class VacationController {

    private final VacationService vacationService;
    private final TeacherService teacherService;

    public VacationController(VacationService vacationService, TeacherService teacherService) {
        this.vacationService = vacationService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String getPage(Model model, Pageable pageable, @PathVariable int teacherId) {
        model.addAttribute("page", vacationService.getByTeacherId(pageable, teacherId));
        model.addAttribute("teacherId", teacherId);
        return "vacations/all";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id, @PathVariable int teacherId) {
        model.addAttribute("vacation", vacationService.get(id));
        model.addAttribute("teacherId", teacherId);
        return "vacations/vacation";
    }

    @GetMapping("/new")
    public String createNew(@PathVariable("teacherId") int teacherId, Model model, @ModelAttribute("vacation") Vacation vacation) {
        model.addAttribute("teacherId", teacherId);
        return "vacations/new";
    }

    @PostMapping("/new")
    public String create(@PathVariable("teacherId") int teacherId, @ModelAttribute("vacation") Vacation vacation) {
        vacation.setTeacher(teacherService.get(teacherId));
        vacationService.create(vacation);
        return "redirect:/teachers/{teacherId}/get";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("teacherId") int teacherId, @PathVariable("id") int id, Model model) {
        model.addAttribute("vacation", vacationService.get(id));
        model.addAttribute("teacherId", teacherId);
        return "vacations/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("teacherId") int teacherId, @ModelAttribute("vacation") Vacation vacation) {
        vacation.setTeacher(teacherService.get(teacherId));
        vacationService.update(vacation);
        return "redirect:/teachers/{teacherId}/get";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("teacherId") int teacherId, @PathVariable("id") int id) {
        vacationService.delete(id);
        return "redirect:/teachers/{teacherId}/get";
    }
}
