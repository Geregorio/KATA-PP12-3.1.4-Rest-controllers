package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserPage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("rolesList", user.getShortRoles());
        model.addAttribute("rolesString", String.join(" ", user.getShortRoles()));
        model.addAttribute("activeSection", "user");
        model.addAttribute("pageTitle", "User page");
        return "base3";
    }

//    @GetMapping
//    public String getUserInfo(Model model, Principal principal) {
//        model.addAttribute("user", userService.findByEmail(principal.getName()));
//        return "base3";
//    }
}