package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "allUsers";
    }

    @GetMapping("/add")
    public String inputNewUser(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }
    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.save(user);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid data. Please check your inputs.");
            return "addUser";
        }
    }

    @GetMapping("/remove")
    public String showRemoveTable(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "removeUser";
    }
    @PostMapping("/remove/delete")
    public String removeUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/remove";
    }

    @GetMapping("/edit")
    public String showEditUserPage(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "editUser";
    }
    @PostMapping("/edit")
    public String editUser(@RequestParam("firstName") String firstName,
                           @RequestParam("secondName") String secondName,
                           @RequestParam(value = "age", required = false, defaultValue = "0") Integer age,
                           @RequestParam("id") Long id,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password) {
        userService.editUser(firstName, secondName, age, id, email, password);
        return "redirect:/admin/edit";
    }
}
