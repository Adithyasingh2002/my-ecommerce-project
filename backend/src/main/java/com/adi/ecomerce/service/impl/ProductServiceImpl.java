package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.OrderItemRepository;
import com.adi.ecomerce.dao.ProductRepository;
import com.adi.ecomerce.entities.OrderItem;
import com.adi.ecomerce.entities.Product;
import com.adi.ecomerce.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Product saveProduct(Product product) {
        logger.info("Saving new product: {}", product.getName());
        Product saved = productRepository.save(product);
        logger.debug("Product saved: {}", saved);
        return saved;
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        logger.debug("Total products found: {}", products.size());
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        logger.info("Fetching product by ID: {}", id);
        return productRepository.findById(id)
                .map(product -> {
                    logger.debug("Product found: {}", product);
                    return product;
                })
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new EntityNotFoundException("Product not found with ID: " + id);
                });
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        logger.info("Updating product with ID: {}", id);
        Product product = getProductById(id);

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());
        product.setCategory(updatedProduct.getCategory());
        product.setImageUrl(updatedProduct.getImageUrl());
        product.setIsActive(updatedProduct.getIsActive());

        Product updated = productRepository.save(product);
        logger.debug("Product updated: {}", updated);
        return updated;
    }

    @Override
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        Product product = getProductById(id);

        //  Remove dependent order items
        List<OrderItem> relatedOrderItems = orderItemRepository.findByProduct(product);
        if (!relatedOrderItems.isEmpty()) {
            orderItemRepository.deleteAll(relatedOrderItems);
            logger.debug("Deleted {} order items related to product ID: {}", relatedOrderItems.size(), id);
        }

        // delete the product
        productRepository.delete(product);
        logger.debug("Product deleted: {}", product);
    }
}
