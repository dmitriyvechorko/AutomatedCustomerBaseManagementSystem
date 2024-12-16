package com.lw2.service;

import com.lw2.entity.Order;
import com.lw2.entity.User;
import com.lw2.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAllByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public void createOrder(Order order) {
        orderRepository.save(order); // Сохраняем заказ в базе
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);  // Получаем все заказы для пользователя
    }

    public void updateOrder(Order order) {
        // Проверка, что заказ существует в базе
        Optional<Order> existingOrder = orderRepository.findById(order.getId());
        if (existingOrder.isPresent()) {
            Order orderToUpdate = existingOrder.get();

            // Обновление всех полей
            orderToUpdate.setTheme(order.getTheme());
            orderToUpdate.setDetails(order.getDetails());
            orderToUpdate.setStatus(order.getStatus());

            // Сохранение обновленного заказа
            orderRepository.save(orderToUpdate);
        } else {
            throw new EntityNotFoundException("Order with id " + order.getId() + " not found");
        }
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll(); // Если вы используете JPA, этот метод уже доступен
    }
}
