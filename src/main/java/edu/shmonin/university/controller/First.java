package edu.shmonin.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class First {

    @GetMapping("/")
    public String first(){
        return "index";
    }
}
