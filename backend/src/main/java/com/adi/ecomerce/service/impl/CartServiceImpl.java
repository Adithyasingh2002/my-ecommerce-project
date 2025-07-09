package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.CartItemRepository;
import com.adi.ecomerce.dao.CartRepository;
import com.adi.ecomerce.entities.*;
import com.adi.ecomerce.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart getCartByUser(User user) {
        logger.info("Fetching cart for user ID: {}", user.getId());
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            logger.info("No cart found. Creating new cart for user ID: {}", user.getId());
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalPrice(0.0);
            return cartRepository.save(newCart);
        });
        logger.debug("Cart retrieved: {}", cart);
        return cart;
    }

    @Override
    public Cart addToCart(User user, Product product, int quantity) {
        logger.info("Adding product ID: {} (qty: {}) to cart for user ID: {}", product.getId(), quantity, user.getId());
        Cart cart = getCartByUser(user);
        boolean productExists = false;

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                logger.debug("Product already in cart. Increased quantity to {}", item.getQuantity());
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
            logger.debug("Added new item to cart: {}", newItem);
        }

        updateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        logger.debug("Cart after addition: {}", savedCart);
        return savedCart;
    }

    @Override
    public Cart removeFromCart(User user, Long productId) {
        logger.info("Removing product ID: {} from cart for user ID: {}", productId, user.getId());
        Cart cart = getCartByUser(user);

        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                cartItemRepository.delete(item);
                logger.debug("Removed item: {}", item);
                break;
            }
        }

        updateCartTotal(cart);
        Cart updatedCart = cartRepository.save(cart);
        logger.debug("Cart after removal: {}", updatedCart);
        return updatedCart;
    }

    @Override
    public Cart clearCart(User user) {
        logger.info("Clearing cart for user ID: {}", user.getId());
        Cart cart = getCartByUser(user);
        cartItemRepository.deleteAll(cart.getItems()); // delete from DB before clearing the list
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        Cart clearedCart = cartRepository.save(cart);
        logger.debug("Cart cleared: {}", clearedCart);
        return clearedCart;
    }

    private void updateCartTotal(Cart cart) {
        double total = 0.0;
        for (CartItem item : cart.getItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        cart.setTotalPrice(total);
        logger.debug("Cart total updated to: {}", total);
    }
}
