package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class RestAdminController {

    private final UserService userService;

    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/get_user")
    public ResponseEntity<User> getUser(@RequestParam("id") Long id) {
        if (userService.existsById(id)) {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nextId")
    public ResponseEntity<Long> getNextId() {
        Long nextId = userService.getMaxId() + 1; // метод, получающий максимальный ID
        return ResponseEntity.ok(nextId);
    }

    @GetMapping("/all_users")
    public ResponseEntity<List<User>> getAllUsers() {
        final List<User> userList = userService.findAll();
        return userList != null && !userList.isEmpty()
                ? new ResponseEntity<>(userList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user,
                                     @RequestParam String selectedRole) {
        if (userService.existsById(user.getId())) {
            return new ResponseEntity<>(HttpStatus.IM_USED);
        } else {
            userService.save(user, selectedRole);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
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
            //user.setId(id);
            userService.save(user, selectedRole);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}
