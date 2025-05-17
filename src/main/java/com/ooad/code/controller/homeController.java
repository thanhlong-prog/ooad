package com.ooad.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homeController {
    @RequestMapping("/add")
    public String add() {
        return "add";
    }

    @RequestMapping("/confirm")
    public String confirm() {
        return "confirm_join";
    }

    @RequestMapping("/list")
    public String list() {
        return "list";
    }

    @RequestMapping("/conflict")
    public String conflict() {
        return "conflict";
    }
}
