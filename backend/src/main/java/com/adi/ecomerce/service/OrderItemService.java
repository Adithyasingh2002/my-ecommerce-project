package com.adi.ecomerce.service;

import com.adi.ecomerce.entities.OrderItem;
import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    OrderItem saveOrderItem(OrderItem orderItem);
    List<OrderItem> getAllOrderItems();
    Optional<OrderItem> getOrderItemById(Long id);
    List<OrderItem> getItemsByOrderId(Long orderId);
    OrderItem updateOrderItem(Long id, OrderItem orderItem);
    void deleteOrderItem(Long id);
}
