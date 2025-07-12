package com.adi.ecomerce.dao;

import com.adi.ecomerce.entities.Order;
import com.adi.ecomerce.entities.OrderStatus;
import com.adi.ecomerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    //  method to exclude CANCELLED orders
    List<Order> findByUserAndStatusNot(User user, OrderStatus status);
}
