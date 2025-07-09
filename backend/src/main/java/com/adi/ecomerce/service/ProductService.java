package com.adi.ecomerce.service;

import com.adi.ecomerce.entities.Product;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long id);  // ✅ fixed

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
