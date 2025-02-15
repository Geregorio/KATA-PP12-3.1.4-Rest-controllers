package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all_users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user,
                                     @RequestParam String selectedRole) {
        userService.save(user, selectedRole);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@RequestParam("id") Long id,
                                      @RequestBody User user,
                                      @RequestParam String selectedRole) {
        if (userService.existsById(id)) {
            userService.save(user, selectedRole);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}
