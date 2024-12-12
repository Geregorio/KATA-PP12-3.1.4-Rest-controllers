package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public void initRoles();
    public User findBySecondName(String secondName);
    public List<User> findAll();
    public void save(User user);
    public void deleteById(Long id);
    public User getUserById(Long id);
    public void editUser(String firstName,
                         String secondName,
                         Integer age,
                         Long id,
                         String email,
                         String password);
}
