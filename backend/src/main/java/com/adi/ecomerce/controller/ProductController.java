package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.Product;
import com.adi.ecomerce.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    // ✅ Only ADMIN can create product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Creating new product: {}", product);
        Product created = productService.saveProduct(product);
        logger.debug("Product created: {}", created);
        return ResponseEntity.ok(created);
    }

    // ✅ Everyone can fetch products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productService.getAllProducts();
        logger.debug("Total products found: {}", products.size());
        return ResponseEntity.ok(products);
    }

    // ✅ Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Fetching product with ID: {}", id);
        try {
            Product product = productService.getProductById(id);
            logger.debug("Product found: {}", product);
            return ResponseEntity.ok(product);
        } catch (EntityNotFoundException e) {
            logger.warn("Product not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Only ADMIN can update product
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Updating product with ID: {}", id);
        try {
            Product updated = productService.updateProduct(id, product);
            logger.debug("Product updated: {}", updated);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            logger.error("Product not found for update, ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Only ADMIN can delete product
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID: {}", id);
        try {
            productService.deleteProduct(id);
            logger.debug("Product deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.error("Product not found for deletion, ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
