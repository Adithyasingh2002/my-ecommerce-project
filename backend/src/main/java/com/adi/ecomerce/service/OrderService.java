package com.adi.ecomerce.service;

import com.adi.ecomerce.dto.OrderResponseDTO;
import com.adi.ecomerce.entities.Order;
import com.adi.ecomerce.entities.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    // Place a new order (typically from frontend)
    Order placeOrder(Order order);

    // Return all order entities (internal/admin use)
    List<Order> getAllOrders();

    // Return all orders as response DTOs (for frontend)
    List<OrderResponseDTO> getAllOrderDTOs();

    // Get order entity by ID
    Optional<Order> getOrderById(Long id);

    // Get order DTO by ID (for frontend)
    Optional<OrderResponseDTO> getOrderDTOById(Long id);

    // Cancel an order by ID (with user verification)
    Order cancelOrder(Long id, String currentUserEmail, boolean isAdmin);

    // Delete an order by ID (admin or internal use)
    void deleteOrder(Long id);

    // Get raw order entities by user (internal use)
    List<Order> getOrdersByUser(User user);

    // Get order DTOs by user (for frontend display)
    List<OrderResponseDTO> getOrderDTOsByUser(User user);

    // Save an order (used during creation or update)
    Order save(Order order);
}
