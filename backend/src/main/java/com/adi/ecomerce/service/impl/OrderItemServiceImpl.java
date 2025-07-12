package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.OrderItemRepository;
import com.adi.ecomerce.entities.OrderItem;
import com.adi.ecomerce.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        logger.info("Saving new order item: {}", orderItem);
        OrderItem saved = orderItemRepository.save(orderItem);
        logger.debug("Order item saved: {}", saved);
        return saved;
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        logger.info("Fetching all order items");
        List<OrderItem> items = orderItemRepository.findAll();
        logger.debug("Total order items found: {}", items.size());
        return items;
    }

    @Override
    public Optional<OrderItem> getOrderItemById(Long id) {
        logger.info("Fetching order item by ID: {}", id);
        Optional<OrderItem> item = orderItemRepository.findById(id);
        if (item.isPresent()) {
            logger.debug("Order item found: {}", item.get());
        } else {
            logger.warn("Order item not found with ID: {}", id);
        }
        return item;
    }

    @Override
    public List<OrderItem> getItemsByOrderId(Long orderId) {
        logger.info("Fetching order items by order ID: {}", orderId);
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        logger.debug("Found {} items for order ID {}", items.size(), orderId);
        return items;
    }

    @Override
    public OrderItem updateOrderItem(Long id, OrderItem updatedItem) {
        logger.info("Updating order item with ID: {}", id);
        OrderItem existing = orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order item not found for update, ID: {}", id);
                    return new EntityNotFoundException("OrderItem not found with ID: " + id);
                });

        existing.setProduct(updatedItem.getProduct());
        existing.setQuantity(updatedItem.getQuantity());
        existing.setPrice(updatedItem.getPrice());

        OrderItem updated = orderItemRepository.save(existing);
        logger.debug("Order item updated: {}", updated);
        return updated;
    }

    @Override
    public void deleteOrderItem(Long id) {
        logger.info("Deleting order item with ID: {}", id);
        if (!orderItemRepository.existsById(id)) {
            logger.error("Order item not found for deletion, ID: {}", id);
            throw new EntityNotFoundException("OrderItem not found with ID: " + id);
        }
        orderItemRepository.deleteById(id);
        logger.debug("Order item deleted with ID: {}", id);
    }
}
