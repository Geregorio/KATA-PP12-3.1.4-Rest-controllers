package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

///////// BootStrap  ///////

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
        return "base3";
    }

    ////////////// Add User //////////////////////
    @GetMapping("/add")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "base3"; // Возвращаем base3, так как новая вкладка будет отображаться внутри этой страницы
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        try {
            // Получаем роль из строки
            String roleName = user.getRoles().toString();  // Получаем строку, например, 'ROLE_USER'
            Role role = userService.getRoleRepository().findByName(roleName); // Ищем роль в базе по имени
            user.setRoles(Collections.singleton(role)); // Присваиваем пользователю роль
            userService.save(user);
            return "redirect:/admin"; // Перенаправляем на страницу администратора
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid data. Please check your inputs.");
            return "base3"; // Возвращаем к форме добавления
        }
    }



    /*
    @GetMapping
    public String getAdminPanel(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<String> rolesList = user.getShortRoles();
        String rolesString = String.join(" ", user.getShortRoles());
        model.addAttribute("rolesList", rolesList);
        model.addAttribute("rolesString", rolesString);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Admin panel");
        return "base3"; // Базовый шаблон
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("userList", users);
        model.addAttribute("pageTitle", "Users");
        return "fragments :: users"; // Ссылка на фрагмент
    }

     */
//////////////////////////////////////////////////////

    //    @GetMapping
    @GetMapping("/getUsers")
    public String getAllUsers(Model model, Principal principal) {
        model.addAttribute("userList", userService.findAll());
        return "allUsers2";
    }

//    @GetMapping("/add")
//    public String inputNewUser(Model model) {
//        model.addAttribute("user", new User());
//        return "addUser";
//    }
//    @PostMapping("/add")
//    public String addUser(@ModelAttribute("user") User user, Model model) {
//        try {
//            userService.save(user);
//            return "redirect:/admin";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Invalid data. Please check your inputs.");
//            return "addUser";
//        }
//    }

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
        User changedUser = userService.getUserById(id);
        changedUser.setFirstName(firstName);
        changedUser.setSecondName(secondName);
        changedUser.setAge(age);
        changedUser.setEmail(email);
        changedUser.setPassword(password);
        userService.editUser(changedUser);
        return "redirect:/admin/edit";
    }
}
