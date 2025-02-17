package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {
    void initRoles();
    User findUserByEmail(String email);
    List<User> findAllUsers();
    void saveUser(User user);
    void saveUser(User user, String selectedRole);
    void deleteUserById(Long id);
    User getUserById(Long id);
    RoleRepository getRoleRepository();
    boolean existsById(Long id);
    boolean existsByPrincipal(Principal principal);
    User getUser(Principal principal);

}
