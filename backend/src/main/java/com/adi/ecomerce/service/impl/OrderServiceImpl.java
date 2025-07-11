package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.OrderRepository;
import com.adi.ecomerce.dao.ProductRepository;
import com.adi.ecomerce.dto.OrderItemDTO;
import com.adi.ecomerce.dto.OrderResponseDTO;
import com.adi.ecomerce.dto.UserInfoDTO;
import com.adi.ecomerce.entities.*;
import com.adi.ecomerce.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order placeOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        double total = 0.0;

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                Long productId = item.getProduct() != null ? item.getProduct().getId() : null;

                if (productId == null) {
                    throw new IllegalArgumentException("Product ID must not be null in order item.");
                }

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

                item.setProduct(product);
                item.setPrice(product.getPrice());
                item.setOrder(order);

                total += item.getPrice() * item.getQuantity();
            }
        }

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderResponseDTO> getAllOrderDTOs() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Optional<OrderResponseDTO> getOrderDTOById(Long id) {
        return getOrderById(id).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id, String currentEmail, boolean isAdmin) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        if (!isAdmin && !order.getUser().getEmail().equalsIgnoreCase(currentEmail)) {
            throw new SecurityException("You are not authorized to cancel this order.");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled.");
        }

        order.setCancelled(true);
        order.setCancelledAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User or user email cannot be null.");
        }

        return orderRepository.findByUserAndStatusNot(user, OrderStatus.CANCELLED);
    }

    @Override
    public List<OrderResponseDTO> getOrderDTOsByUser(User user) {
        return getOrdersByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    private OrderResponseDTO convertToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);
        dto.setStatus(order.getStatus().name());
        dto.setCancelled(order.isCancelled());
        dto.setCancelledAt(order.getCancelledAt());
        dto.setCreatedAt(order.getOrderDate());
        dto.setOrderDate(order.getOrderDate());

        // User info
        User user = order.getUser();
        if (user != null) {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setId(user.getId());
            userInfoDTO.setFullName(user.getFullName());
            userInfoDTO.setEmail(user.getEmail());
            userInfoDTO.setPhoneNumber(user.getPhoneNumber());
            userInfoDTO.setAddress(user.getAddress());

            String role = (user.getRoles() != null && !user.getRoles().isEmpty())
                    ? user.getRoles().stream().findFirst().orElse("USER")
                    : "USER";
            userInfoDTO.setRole(role);

            dto.setUser(userInfoDTO);
        }

        // Order items
        List<OrderItemDTO> itemDTOs = order.getOrderItems() != null
                ? order.getOrderItems().stream().map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());

                    Long productId = item.getProduct() != null ? item.getProduct().getId() : null;
                    itemDTO.setProductId(productId);

                    if (productId != null) {
                        Product product = productRepository.findById(productId).orElse(null);
                        itemDTO.setProductName(product != null && product.getName() != null
                                ? product.getName()
                                : "Unnamed Product");
                    } else {
                        itemDTO.setProductName("Unknown Product");
                    }

                    return itemDTO;
                }).collect(Collectors.toList())
                : Collections.emptyList();

        dto.setItems(itemDTOs);
        return dto;
    }
}
