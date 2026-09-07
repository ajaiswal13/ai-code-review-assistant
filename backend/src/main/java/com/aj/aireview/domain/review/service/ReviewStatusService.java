package com.aj.aireview.domain.review.service;

import com.aj.aireview.application.review.event.ReviewStatusChangedEvent;
import com.aj.aireview.domain.review.entity.Review;
import com.aj.aireview.domain.review.entity.ReviewStatus;
import com.aj.aireview.domain.review.exception.ReviewNotFoundException;
import com.aj.aireview.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewStatusService {

    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewStatusService(
            ReviewRepository reviewRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.reviewRepository = reviewRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void markProcessing(UUID reviewId) {
        Review review = getReview(reviewId);

        review.markProcessing();

        eventPublisher.publishEvent(
                new ReviewStatusChangedEvent(
                        reviewId,
                        ReviewStatus.PROCESSING
                )
        );
    }

    @Transactional
    public void markFailed(UUID reviewId) {
        Review review = getReview(reviewId);

        review.markFailed();

        eventPublisher.publishEvent(
                new ReviewStatusChangedEvent(
                        reviewId,
                        ReviewStatus.FAILED
                )
        );
    }

    private Review getReview(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
    }
}