package com.aj.aireview.domain.review.repository;

import com.aj.aireview.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Review> findByIdAndUserId(UUID reviewId, UUID userId);
}
