package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAdminPage(@RequestParam(value = "activeTab", defaultValue = "usersTable") String activeTab,
            Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("rolesList", user.getShortRoles());
        model.addAttribute("rolesString", String.join(" ", user.getShortRoles()));
        model.addAttribute("activeSection", "admin");
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("userList", userService.findAll()); // Список пользователей
        model.addAttribute("pageTitle", "Admin Panel");
        return "base";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user,
                          @ModelAttribute("selectedRole") String selectedRole,
                          Model model) {
        try {
            userService.save(user, selectedRole);
            return "redirect:/admin?activeTab=usersTable";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid data. Please check your inputs.");
            model.addAttribute("activeTab", "newUser");
            return "base";
        }
    }

    @PostMapping("/delete")
    public String removeUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin?activeTab=usersTable";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user,
                           @RequestParam("selectedRole") String selectedRole) {
        userService.save(user, selectedRole);
        return "redirect:/admin?activeTab=usersTable";
    }
}
