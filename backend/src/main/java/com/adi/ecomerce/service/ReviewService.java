package com.adi.ecomerce.service;

import com.adi.ecomerce.entities.Review;
import java.util.List;

public interface ReviewService {

    Review addReview(Long userId, Long productId, Review review);

    List<Review> getReviewsByProductId(Long productId);

    void deleteReview(Long reviewId);

    //  Get all reviews (for admin panel)
    List<Review> getAllReviews();
}
