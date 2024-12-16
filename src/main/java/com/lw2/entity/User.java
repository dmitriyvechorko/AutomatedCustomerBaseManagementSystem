package com.lw2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Data  // Генерирует геттеры, сеттеры, toString, equals и hashCode
@NoArgsConstructor  // Генерирует конструктор без аргументов
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;
    private String gender;
    private String email;
    private String socialMedia;
    private String notes;
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Мы возвращаем роль пользователя
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Для простоты, аккаунт не истекает
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Для простоты, аккаунт не блокируется
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Для простоты, учетные данные не истекают
    }

    @Override
    public boolean isEnabled() {
        return true; // Для простоты, пользователь всегда активен
    }

    public enum Role {
        CLIENT,
        EMPLOYEE
    }
}
