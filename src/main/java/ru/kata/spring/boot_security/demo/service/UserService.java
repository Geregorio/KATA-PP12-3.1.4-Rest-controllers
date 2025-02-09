package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;

public interface UserService extends UserDetailsService {
    void initRoles();
    User findByEmail(String email);
    List<User> findAll();
    void save(User user);
    void save(User user, String selectedRole);
    boolean deleteById(Long id);
    User getUserById(Long id);
    RoleRepository getRoleRepository();
    boolean existsById(Long id);
    Long getMaxId();
}
