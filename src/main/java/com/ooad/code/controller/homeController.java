package com.ooad.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homeController {
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/register")
    public String register() {
        return "register";
    }
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
    @RequestMapping("/header")
    public String header() {
        return "header";
    }
}
