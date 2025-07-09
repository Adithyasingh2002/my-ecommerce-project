package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.Cart;
import com.adi.ecomerce.entities.Product;
import com.adi.ecomerce.entities.User;
import com.adi.ecomerce.service.CartService;
import com.adi.ecomerce.service.ProductService;
import com.adi.ecomerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    // ✅ GET cart by user ID
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        logger.info("📦 Fetching cart for userId: {}", userId);
        User user = userService.getUserById(userId);
        Cart cart = cartService.getCartByUser(user);
        logger.info("✅ Cart fetched for userId: {}", userId);
        return cart;
    }

    // ✅ ADD product to cart
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/add")
    public Cart addToCart(@RequestParam Long userId,
                          @RequestParam Long productId,
                          @RequestParam int quantity) {
        logger.info("➕ Adding productId {} (qty: {}) to userId {}'s cart", productId, quantity, userId);
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        Cart cart = cartService.addToCart(user, product, quantity);
        logger.info("✅ Product added to cart for userId: {}", userId);
        return cart;
    }

    // ✅ REMOVE product from cart
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/remove")
    public Cart removeFromCart(@RequestParam Long userId,
                               @RequestParam Long productId) {
        logger.info("❌ Removing productId {} from userId {}'s cart", productId, userId);
        User user = userService.getUserById(userId);
        Cart cart = cartService.removeFromCart(user, productId);
        logger.info("✅ Product removed from cart for userId: {}", userId);
        return cart;
    }

    // ✅ CLEAR cart
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/clear")
    public Cart clearCart(@RequestParam Long userId) {
        logger.info("🧹 Clearing cart for userId: {}", userId);
        User user = userService.getUserById(userId);
        Cart cart = cartService.clearCart(user);
        logger.info("✅ Cart cleared for userId: {}", userId);
        return cart;
    }
}
