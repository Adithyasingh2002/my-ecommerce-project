package com.adi.ecomerce.service;

import com.adi.ecomerce.entities.Cart;
import com.adi.ecomerce.entities.User;
import com.adi.ecomerce.entities.Product;

public interface CartService {
    Cart getCartByUser(User user);
    Cart addToCart(User user, Product product, int quantity);
    Cart removeFromCart(User user, Long productId);
    Cart clearCart(User user);
}
