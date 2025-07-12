package com.adi.ecomerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private UserInfoDTO user;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private boolean cancelled;
    private double totalAmount;
    private List<OrderItemDTO> items;

    public OrderResponseDTO() {}

    public OrderResponseDTO(Long id, UserInfoDTO user, String status,
                            LocalDateTime orderDate, double totalAmount,
                            List<OrderItemDTO> items,
                            LocalDateTime createdAt, LocalDateTime cancelledAt,
                            boolean cancelled) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items;
        this.createdAt = createdAt;
        this.cancelledAt = cancelledAt;
        this.cancelled = cancelled;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserInfoDTO getUser() {
        return user;
    }

    public void setUser(UserInfoDTO user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
