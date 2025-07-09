package com.adi.ecomerce.dao;

import com.adi.ecomerce.entities.Review;
import com.adi.ecomerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
}
