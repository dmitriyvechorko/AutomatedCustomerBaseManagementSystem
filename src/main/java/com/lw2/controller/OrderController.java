package com.lw2.controller;

import com.lw2.dto.OrderDTO;
import com.lw2.entity.Order;
import com.lw2.entity.User;
import com.lw2.enums.OrderStatus;
import com.lw2.service.OrderService;
import com.lw2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String getOrdersPage(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            User currentUser = user.get();
            List<Order> orders;

            // Работник видит все заказы, клиент — только свои
            if (currentUser.getRole() == User.Role.EMPLOYEE) {
                orders = orderService.findAllOrders(); // Для работников
            } else {
                orders = orderService.findAllByUser(currentUser); // Для клиентов
            }

            model.addAttribute("orders", orders);
            model.addAttribute("isEmployee", currentUser.getRole() == User.Role.EMPLOYEE);
            return "orders";
        }
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String getEditOrderPage(@PathVariable Long id, Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> user = userService.findByUsername(username);
        Optional<Order> order = orderService.findById(id);

        if (user.isPresent() && order.isPresent()) {
            boolean isEmployee = user.get().getRole() == User.Role.EMPLOYEE;

            // Проверка, может ли пользователь редактировать заказ
            if (order.get().getUser().getUsername().equals(username) || isEmployee) {
                model.addAttribute("order", order.get());  // Передаем заказ в модель
                model.addAttribute("isEmployee", isEmployee);  // Передаем флаг, если пользователь - сотрудник
                model.addAttribute("statuses", OrderStatus.values());  // Передаем все статусы для работника
                return "orders";  // Возвращаем представление с заказами
            }
        }

        return "redirect:/orders";  // Перенаправляем на страницу заказов, если нет доступа
    }


    @PostMapping("/{id}/edit")
    public String editOrder(@PathVariable Long id, @ModelAttribute("orderDTO") OrderDTO orderDTO, Principal principal) {
        String username = principal.getName();
        Optional<User> user = userService.findByUsername(username);
        Optional<Order> order = orderService.findById(id);

        if (user.isPresent() && order.isPresent()) {
            if (user.get().getRole() == User.Role.EMPLOYEE) {
                Order orderToUpdate = order.get();
                orderToUpdate.setTheme(orderDTO.getTheme());
                orderToUpdate.setDetails(orderDTO.getDetails());
                orderToUpdate.setStatus(orderDTO.getStatus());
                orderService.updateOrder(orderToUpdate);
            }
        }
        return "redirect:/orders";
    }




    // Отображение страницы создания заказа
    @GetMapping("/create")
    public String showCreateOrderPage(Model model) {
        model.addAttribute("order", new OrderDTO()); // Используем DTO для передачи данных
        return "createOrder";
    }

    // Обработка формы создания заказа
    @PostMapping("/create")
    public String createOrder(@ModelAttribute("orderDTO") OrderDTO orderDTO, Principal principal) {
        String username = principal.getName(); // Получаем имя текущего пользователя
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Создаём сущность Order из OrderDTO
        Order order = new Order();
        order.setTheme(orderDTO.getTheme());
        order.setDetails(orderDTO.getDetails());
        order.setStatus(OrderStatus.ACTIVE);
        order.setUser(user); // Устанавливаем пользователя

        // Сохраняем заказ
        orderService.createOrder(order);

        return "redirect:/orders";  // Перенаправление на страницу с заказами
    }
}
