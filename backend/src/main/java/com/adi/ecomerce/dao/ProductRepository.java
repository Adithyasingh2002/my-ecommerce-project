package com.adi.ecomerce.dao;

import com.adi.ecomerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // You can also add custom query methods here if needed, for example:
    // List<Product> findByCategory(String category);
}
