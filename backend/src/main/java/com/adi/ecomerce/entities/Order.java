package com.adi.ecomerce.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "orders") // avoid using "order" directly
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate = LocalDateTime.now();
    private Double totalAmount = 0.0;
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    private boolean cancelled = false;
    private LocalDateTime cancelledAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "roles", "orders"})
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    public Order(LocalDateTime orderDate, Double totalAmount, OrderStatus status, User user) {
        this.orderDate = orderDate != null ? orderDate : LocalDateTime.now();
        this.totalAmount = totalAmount != null ? totalAmount : 0.0;
        this.status = status != null ? status : OrderStatus.PENDING;
        this.user = user;
    }
      //Calculates total amount from order items.
    public void calculateTotalAmount() {
        if (orderItems != null && !orderItems.isEmpty()) {
            this.totalAmount = orderItems.stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
        } else {
            this.totalAmount = 0.0;
        }
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", cancelled=" + cancelled +
                ", cancelledAt=" + cancelledAt +
                ", user=" + (user != null ? user.getEmail() : "null") +
                '}';
    }
}
