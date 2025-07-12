package com.adi.ecomerce.controller;

import com.adi.ecomerce.dto.OrderResponseDTO;
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

    //  Place a new order (User only)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findFirstByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found."));

            order.setUser(user);
            Order placedOrder = orderService.placeOrder(order);
            return ResponseEntity.ok(placedOrder);
        } catch (Exception e) {
            logger.error("❌ Failed to place order", e);
            return ResponseEntity.status(500).body("Error placing order: " + e.getMessage());
        }
    }

    //  Get current user's orders
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersForCurrentUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findFirstByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found."));

            List<OrderResponseDTO> userOrders = orderService.getOrderDTOsByUser(user);
            return ResponseEntity.ok(userOrders);
        } catch (Exception e) {
            logger.error("❌ Failed to fetch user orders", e);
            return ResponseEntity.status(500).build();
        }
    }

    //  Admin: Get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderResponseDTO> allOrders = orderService.getAllOrderDTOs();
            return ResponseEntity.ok(allOrders);
        } catch (Exception e) {
            logger.error("❌ Failed to fetch orders", e);
            return ResponseEntity.status(500).body("Error retrieving orders: " + e.getMessage());
        }
    }

    //  Get specific order
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Optional<OrderResponseDTO> orderOpt = orderService.getOrderDTOById(id);
            return orderOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("❌ Failed to fetch order by ID", e);
            return ResponseEntity.status(500).body("Error retrieving order: " + e.getMessage());
        }
    }

    //  Cancel order (owner or admin)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            Order cancelledOrder = orderService.cancelOrder(id, email, isAdmin);
            return ResponseEntity.ok(cancelledOrder);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Failed to cancel order", e);
            return ResponseEntity.status(500).body("Error cancelling order: " + e.getMessage());
        }
    }

    //  Delete order completely (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("❌ Failed to delete order", e);
            return ResponseEntity.status(500).body("Error deleting order: " + e.getMessage());
        }
    }
}
