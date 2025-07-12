package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.OrderItem;
import com.adi.ecomerce.service.OrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    @Autowired
    private OrderItemService orderItemService;

    //  ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        logger.info("Fetching all order items");
        List<OrderItem> items = orderItemService.getAllOrderItems();
        logger.debug("Total order items fetched: {}", items.size());
        return ResponseEntity.ok(items);
    }

    //  ADMIN and USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getItemsByOrderId(@PathVariable Long orderId) {
        logger.info("Fetching order items for order ID: {}", orderId);
        List<OrderItem> items = orderItemService.getItemsByOrderId(orderId);
        logger.debug("Found {} items for order ID {}", items.size(), orderId);
        return ResponseEntity.ok(items);
    }

    //  ADMIN and USER
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        logger.info("Fetching order item with ID: {}", id);
        return orderItemService.getOrderItemById(id)
                .map(item -> {
                    logger.debug("Found order item: {}", item);
                    return ResponseEntity.ok(item);
                })
                .orElseGet(() -> {
                    logger.warn("Order item not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    //  ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        logger.info("Creating new order item: {}", orderItem);
        OrderItem created = orderItemService.saveOrderItem(orderItem);
        logger.debug("Order item created: {}", created);
        return ResponseEntity.ok(created);
    }

    //  ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long id, @RequestBody OrderItem orderItem) {
        logger.info("Updating order item with ID: {}", id);
        OrderItem updated = orderItemService.updateOrderItem(id, orderItem);
        logger.debug("Order item updated: {}", updated);
        return ResponseEntity.ok(updated);
    }

    //  ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        logger.info("Deleting order item with ID: {}", id);
        orderItemService.deleteOrderItem(id);
        logger.debug("Order item deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
