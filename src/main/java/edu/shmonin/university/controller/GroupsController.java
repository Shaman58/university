package edu.shmonin.university.controller;

import edu.shmonin.university.service.GroupService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final GroupService groupService;

    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public String getPages(Model model, Pageable pageable) {
        model.addAttribute("page", groupService.getAll(pageable));
        return "groups/index";
    }

    @GetMapping("/{id}/get")
    public String get(Model model, @PathVariable int id) {
        model.addAttribute("group", groupService.get(id));
        return "groups/group";
    }
}