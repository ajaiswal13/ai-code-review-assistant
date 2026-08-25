package com.aj.aireview.domain.review.service;

import com.aj.aireview.domain.review.entity.Review;
import com.aj.aireview.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewStatusService {

    private final ReviewRepository reviewRepository;

    public ReviewStatusService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void markProcessing(UUID reviewId) {
        Review review = getReview(reviewId);

        review.markProcessing();
    }

    @Transactional
    public void markFailed(UUID reviewId) {
        Review review = getReview(reviewId);

        review.markFailed();
    }

    private Review getReview(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review not found: " + reviewId
                        )
                );
    }
}
