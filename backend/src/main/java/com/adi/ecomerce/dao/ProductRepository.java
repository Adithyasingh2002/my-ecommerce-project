package com.adi.ecomerce.dao;

import com.adi.ecomerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //  Find all products by category (optional custom use)
    List<Product> findByCategory(String category);
    //  Find products by name containing a keyword (case-insensitive search)
    List<Product> findByNameContainingIgnoreCase(String keyword);

  
}
