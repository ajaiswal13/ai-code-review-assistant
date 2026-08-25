package com.aj.aireview.domain.review.dto;

import com.aj.aireview.domain.review.entity.Review;
import com.aj.aireview.domain.review.entity.ReviewStatus;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        String language,
        ReviewStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getLanguage(),
                review.getStatus(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
