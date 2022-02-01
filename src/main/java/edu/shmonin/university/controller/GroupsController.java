package edu.shmonin.university.controller;

import edu.shmonin.university.model.Group;
import edu.shmonin.university.service.GroupService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final GroupService groupService;

    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public String getPages(Model model, Pageable pageable) {
        var groupPages = groupService.getSortedPaginated(pageable);
        model.addAttribute("groupPage", groupPages);
        var totalPages = groupPages.getTotalPages();
        if (totalPages > 1) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "groups/index";
    }

    @GetMapping("/new")
    public String createNew(@ModelAttribute("group") Group group) {
        return "groups/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("group") Group group) {
        groupService.create(group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("group", groupService.get(id));
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") Group group, @PathVariable("id") int id) {
        group.setId(id);
        groupService.update(group);
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        groupService.delete(id);
        return "redirect:/groups";
    }
}