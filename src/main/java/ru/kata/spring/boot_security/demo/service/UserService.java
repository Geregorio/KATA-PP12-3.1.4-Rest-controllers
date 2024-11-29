package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    void addUser(User user);
    void editUser(String firstName,
                  String secondName,
                  Integer age,
                  Long id);
    void removeUser(Long id);
    User getUserById(Long id);
}
