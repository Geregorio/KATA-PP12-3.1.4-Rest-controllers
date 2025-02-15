package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
@JsonIgnoreProperties({"id"})
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonValue
    private String name;

    public Role(Long id, String name){
        this.id = id;
        this.name = name;
    }
    public Role(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(getId(), role.getId()) && Objects.equals(getName(), role.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
