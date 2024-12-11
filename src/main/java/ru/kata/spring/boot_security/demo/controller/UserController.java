package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user")
    @ResponseBody
    public String getUserPage(Principal principal) {
        User user = userService.findBySecondName(principal.getName());
        return "User, welcome spring boot security page! You are: " +
                user.getSecondName() + ", and you are: " +
                user.getRoles();
    }

    @GetMapping(value = "/helloAdmin")
    @ResponseBody
    public String getAdminPage(Principal principal) {
        User user = userService.findBySecondName(principal.getName());
        return "Admin, welcome spring boot security page! You are: " +
                user.getSecondName() + ", and you are: " +
                user.getRoles();
    }

    @GetMapping(value = "/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "users";
    }

    @GetMapping("/admin/add")
    public String inputNewUser(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }
    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.save(user);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid data. Please check your inputs.");
            return "addUser";
        }
    }

    @GetMapping("admin/remove")
    public String showRemoveTable(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "removeUser";
    }
    @PostMapping("/admin/remove/delete")
    public String removeUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/remove";
    }

    @GetMapping("/admin/edit")
    public String showEditUserPage(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "editUser";
    }
    @PostMapping("/admin/edit")
    public String editUser(@RequestParam("firstName") String firstName,
                           @RequestParam("secondName") String secondName,
                           @RequestParam(value = "age", required = false, defaultValue = "0") Integer age,
                           @RequestParam("id") Long id,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email) {
        userService.editUser(firstName, secondName, age, id, password, email);
        return "redirect:/admin/edit";
    }
}