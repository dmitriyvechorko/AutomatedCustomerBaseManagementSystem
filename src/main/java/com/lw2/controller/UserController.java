package com.lw2.controller;

import com.lw2.entity.User;
import com.lw2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Страница профиля

    // Отображение профиля пользователя
    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        String username = principal.getName();  // Получаем имя текущего пользователя
        Optional<User> optionalUser = userService.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);  // Добавляем объект пользователя в модель для отображения
        } else {
            // Обработка случая, если пользователь не найден
            throw new RuntimeException("User not found");
        }

        return "profile";  // Имя шаблона для профиля
    }

    // Обработка редактирования профиля (GET)
    @GetMapping("/profile/edit")
    public String editProfilePage(Model model, Principal principal) {
        String username = principal.getName();  // Получаем имя текущего пользователя
        Optional<User> optionalUser = userService.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);  // Добавляем объект пользователя для редактирования
        } else {
            throw new RuntimeException("User not found");
        }

        return "editProfile";  // Имя шаблона для редактирования профиля
    }

    // Обработка редактирования профиля (POST)
    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute("user") User updatedUser, Principal principal) {
        String username = principal.getName();
        Optional<User> optionalCurrentUser = userService.findByUsername(username);

        if (optionalCurrentUser.isPresent()) {
            User currentUser = optionalCurrentUser.get();

            // Обновляем информацию пользователя
            currentUser.setFullName(updatedUser.getFullName());
            currentUser.setEmail(updatedUser.getEmail());
            currentUser.setSocialMedia(updatedUser.getSocialMedia());
            currentUser.setNotes(updatedUser.getNotes());

            // Сохраняем обновленные данные
            userService.update(currentUser);
        } else {
            throw new RuntimeException("User not found");
        }

        return "redirect:/user/profile";
    }
    @GetMapping("/show")
    public String showClientsPage(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent() && user.get().getRole() == User.Role.EMPLOYEE) {
            // Получаем всех клиентов, передаем только нужные поля
            List<User> clients = userService.findAllByRole(User.Role.CLIENT);
            model.addAttribute("clients", clients);
            return "showClients";  // Шаблон для отображения клиентов
        } else {
            return "redirect:/";  // Перенаправляем на главную страницу, если не работник
        }
    }
}
