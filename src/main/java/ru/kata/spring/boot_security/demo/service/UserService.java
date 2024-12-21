package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;

public interface UserService extends UserDetailsService {
    public void initRoles();
    public User findByEmail(String email);
    public List<User> findAll();
    public void save(User user);
    public void deleteById(Long id);
    public User getUserById(Long id);
    public void editUser(User user);
    public RoleRepository getRoleRepository();
}
