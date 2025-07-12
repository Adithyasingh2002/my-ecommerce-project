package com.adi.ecomerce.dao;

import com.adi.ecomerce.entities.Cart;
import com.adi.ecomerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
