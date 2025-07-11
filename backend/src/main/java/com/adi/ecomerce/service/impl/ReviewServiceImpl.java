package com.adi.ecomerce.service.impl;

import com.adi.ecomerce.dao.ProductRepository;
import com.adi.ecomerce.dao.ReviewRepository;
import com.adi.ecomerce.dao.UserRepository;
import com.adi.ecomerce.entities.Product;
import com.adi.ecomerce.entities.Review;
import com.adi.ecomerce.entities.User;
import com.adi.ecomerce.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Review addReview(Long userId, Long productId, Review review) {
        logger.info("Adding review by user ID: {} for product ID: {}", userId, productId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new EntityNotFoundException("User not found");
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", productId);
                    return new EntityNotFoundException("Product not found");
                });

        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        logger.debug("Review saved: {}", savedReview);
        return savedReview;
    }

    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        logger.info("Fetching reviews for product ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", productId);
                    return new EntityNotFoundException("Product not found");
                });

        List<Review> reviews = reviewRepository.findByProduct(product);
        logger.debug("Total reviews found: {}", reviews.size());
        return reviews;
    }

    @Override
    public void deleteReview(Long reviewId) {
        logger.info("Deleting review with ID: {}", reviewId);
        reviewRepository.deleteById(reviewId);
        logger.debug("Review deleted with ID: {}", reviewId);
    }

    // ✅ NEW: Get all reviews (admin)
    @Override
    public List<Review> getAllReviews() {
        logger.info("Fetching all reviews");
        return reviewRepository.findAll();
    }
}
