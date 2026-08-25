package com.aj.aireview.domain.review.service;

import com.aj.aireview.domain.review.dto.CreateReviewRequest;
import com.aj.aireview.domain.review.dto.ReviewResponse;
import com.aj.aireview.domain.review.entity.Review;
import com.aj.aireview.domain.review.event.ReviewCreatedEvent;
import com.aj.aireview.domain.review.exception.ReviewNotFoundException;
import com.aj.aireview.domain.review.repository.ReviewRepository;
import com.aj.aireview.security.user.AuthenticatedUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewService(
            ReviewRepository reviewRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.reviewRepository = reviewRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ReviewResponse createReview(
            CreateReviewRequest request,
            AuthenticatedUser authenticatedUser
    ) {
        Review review = new Review(
                authenticatedUser.getUser(),
                request.language(),
                request.code()
        );

        Review savedReview = reviewRepository.save(review);

        eventPublisher.publishEvent(
                new ReviewCreatedEvent(savedReview.getId())
        );

        return ReviewResponse.from(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(
            AuthenticatedUser authenticatedUser
    ) {
        UUID userId = authenticatedUser.getUser().getId();

        return reviewRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(
            UUID reviewId,
            AuthenticatedUser authenticatedUser
    ) {
        UUID userId = authenticatedUser.getUser().getId();

        Review review = reviewRepository
                .findByIdAndUserId(reviewId, userId)
                .orElseThrow(ReviewNotFoundException::new);

        return ReviewResponse.from(review);
    }
}
