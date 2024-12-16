package com.lw2.service;

import com.lw2.entity.User;
import com.lw2.exception.NotFoundException;
import com.lw2.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Сохранение пользователя
    public User save(User user) {
        return userRepository.save(user);
    }

    // Удаление пользователя по ID
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // Обновление данных пользователя
    public User update(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not Found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword()); // Кодирование пароля
        existingUser.setEmail(user.getEmail());
        existingUser.setFullName(user.getFullName());
        existingUser.setGender(user.getGender());
        existingUser.setSocialMedia(user.getSocialMedia());
        existingUser.setNotes(user.getNotes());
        existingUser.setPosition(user.getPosition());

        return userRepository.save(existingUser);
    }

    // Реализация метода loadUserByUsername из интерфейса UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new UsernameNotFoundException(username);
    }

    // Поиск пользователя по ID
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    // Поиск пользователя по email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    // Создание нового пользователя
    public User createUser(User user) {
        user.setPassword(user.getPassword()); // Кодирование пароля перед сохранением
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username); // Или другой метод репозитория для поиска по логину
    }

    public List<User> findAllByRole(User.Role role) {
        return userRepository.findAllByRole(role);
    }
}
