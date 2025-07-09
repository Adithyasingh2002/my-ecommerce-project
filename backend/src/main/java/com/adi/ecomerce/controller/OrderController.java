package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.Order;
import com.adi.ecomerce.entities.User;
import com.adi.ecomerce.service.OrderService;
import com.adi.ecomerce.dao.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName(); // email is the principal (username)

            Optional<User> optionalUser = userRepository.findFirstByEmail(email);
            if (optionalUser.isEmpty()) {
                logger.error("❌ User not found in database for email: {}", email);
                return ResponseEntity.badRequest().body("User not found.");
            }

            User user = optionalUser.get();
            order.setUser(user); // Attach authenticated user to order

            logger.info("📦 Placing new order for user: {}", user.getEmail());
            Order placedOrder = orderService.placeOrder(order);
            logger.debug("✅ Order placed: {}", placedOrder);
            return ResponseEntity.ok(placedOrder);

        } catch (Exception e) {
            logger.error("❌ Failed to place order", e);
            return ResponseEntity.status(500).body("Error placing order: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            logger.info("📋 Fetching all orders");
            List<Order> orders = orderService.getAllOrders();
            logger.debug("✅ Total orders found: {}", orders.size());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("❌ Failed to fetch orders", e);
            return ResponseEntity.status(500).body("Error retrieving orders: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        logger.info("🔍 Fetching order with ID: {}", id);
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("⚠️ Order not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            logger.info("❌ Cancelling order with ID: {}", id);
            orderService.cancelOrder(id);
            logger.debug("✅ Order cancelled: {}", id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("⚠️ Order not found to cancel, ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("❌ Failed to cancel order", e);
            return ResponseEntity.status(500).body("Error cancelling order: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            logger.info("🗑️ Permanently deleting order with ID: {}", id);
            orderService.deleteOrder(id);
            logger.debug("✅ Order deleted: {}", id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("⚠️ Order not found to delete, ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("❌ Failed to delete order", e);
            return ResponseEntity.status(500).body("Error deleting order: " + e.getMessage());
        }
    }
}
