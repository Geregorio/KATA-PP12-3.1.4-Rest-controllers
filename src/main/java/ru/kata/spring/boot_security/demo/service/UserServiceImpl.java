package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role (1L, "ROLE_ADMIN"));
            roleRepository.save(new Role (2L, "ROLE_USER"));
        }
    }

    //////////UserDetailService////////////
    @Override
    public UserDetails loadUserByUsername(String secondName) throws UsernameNotFoundException {
        User user = findBySecondName(secondName);
        if (user == null) {
            throw new UsernameNotFoundException("User " + secondName + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getSecondName(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    //////////JpaRepository///////////////
    public User findBySecondName(String secondName) {
        return userRepository.findBySecondName(secondName);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user){
        Role role = roleRepository.findByName("ROLE_USER");
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of(role));
        }
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    /////////////Own///////////////
    public void editUser(String firstName,
                         String secondName,
                         Integer age,
                         Long id,
                         String email,
                         String password) {
        User changedUser = new User();
        changedUser.setId(id);
        changedUser.setFirstName(firstName);
        changedUser.setEmail(secondName);
        changedUser.setAge(age);
        changedUser.setEmail(email);
        changedUser.setPassword(password);
        userRepository.save(changedUser);
    }
}
