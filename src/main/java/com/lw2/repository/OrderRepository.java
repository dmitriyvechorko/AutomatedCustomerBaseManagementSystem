package com.lw2.repository;

import com.lw2.entity.Order;
import com.lw2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long clientId);

    List<Order> findByUser(User user);

}

