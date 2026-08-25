package com.aj.aireview.domain.review.repository;

import com.aj.aireview.domain.review.entity.ReviewResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewResultRepository
        extends JpaRepository<ReviewResult, UUID> {

    Optional<ReviewResult> findByReviewId(UUID reviewId);
}