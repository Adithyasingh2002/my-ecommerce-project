package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.OrderRepository;
import com.adi.ecomerce.dao.ProductRepository;
import com.adi.ecomerce.entities.*;
import com.adi.ecomerce.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order placeOrder(Order order) {
        if (order.getUser() == null) {
            logger.error("❌ User is null in the order request.");
            throw new IllegalArgumentException("User information is required to place an order.");
        }

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        double totalAmount = 0.0;

        for (OrderItem item : order.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new IllegalArgumentException("Product ID is required for each item.");
            }

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + item.getProduct().getId()));

            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setOrder(order);

            totalAmount += product.getPrice() * item.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        Order saved = orderRepository.save(order);
        logger.info("📦 Order placed successfully with ID: {}", saved.getId());
        logger.info("📦 Order contains {} items, total ₹{}", saved.getItems().size(), saved.getTotalAmount());
        return saved;
    }

    @Override
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders from database...");
        List<Order> orders = orderRepository.findAll();
        logger.info("✅ Total orders fetched: {}", orders.size());
        return orders;
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        logger.info("Fetching order by ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);
        logger.info(order.isPresent() ? "✅ Order found" : "⚠️ No order found with ID " + id);
        return order;
    }

    @Override
    public void cancelOrder(Long id) {
        logger.info("Cancelling order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found for cancellation, ID: {}", id);
                    return new EntityNotFoundException("Order not found with ID: " + id);
                });

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        logger.info("✅ Order cancelled successfully, ID: {}", id);
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Attempting to permanently delete order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        orderRepository.delete(order);
        logger.warn("🗑️ Order deleted from database, ID: {}", id);
    }
}
