package edu.shmonin.university.controller;

import edu.shmonin.university.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupsController {

    private final GroupService groupService;

    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public String viewGroups(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "groups";
    }
}
