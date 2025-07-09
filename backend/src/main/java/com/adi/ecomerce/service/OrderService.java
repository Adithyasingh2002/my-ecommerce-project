package com.adi.ecomerce.service;

import com.adi.ecomerce.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order placeOrder(Order order);
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    void cancelOrder(Long id);
    void deleteOrder(Long id); // ✅ NEW
}
