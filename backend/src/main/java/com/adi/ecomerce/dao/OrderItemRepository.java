package com.adi.ecomerce.dao;

import com.adi.ecomerce.entities.OrderItem;
import com.adi.ecomerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    // ✅ New method to find order items by product
    List<OrderItem> findByProduct(Product product);
}
