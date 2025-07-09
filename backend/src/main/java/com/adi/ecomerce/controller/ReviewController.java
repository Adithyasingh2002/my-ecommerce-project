package com.adi.ecomerce.controller;

import com.adi.ecomerce.entities.Review;
import com.adi.ecomerce.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    // ✅ POST review by a user for a product (roles: USER or ADMIN)
    @PostMapping("/{userId}/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Review> createReview(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestBody Review review) {

        logger.info("Creating review by user ID: {} for product ID: {}", userId, productId);
        Review createdReview = reviewService.addReview(userId, productId, review);
        logger.debug("Review created: {}", createdReview);
        return ResponseEntity.ok(createdReview);
    }

    // ✅ GET all reviews for a product (public)
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsForProduct(@PathVariable Long productId) {
        logger.info("Fetching reviews for product ID: {}", productId);
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        logger.debug("Total reviews found for product ID {}: {}", productId, reviews.size());
        return ResponseEntity.ok(reviews);
    }

    // ✅ DELETE a review (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        logger.info("Deleting review with ID: {}", id);
        reviewService.deleteReview(id);
        logger.debug("Review deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
