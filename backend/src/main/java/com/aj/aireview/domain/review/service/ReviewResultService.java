package com.aj.aireview.domain.review.service;

import com.aj.aireview.domain.ai.AIReviewResult;
import com.aj.aireview.domain.review.entity.Review;
import com.aj.aireview.domain.review.entity.ReviewResult;
import com.aj.aireview.domain.review.repository.ReviewRepository;
import com.aj.aireview.domain.review.repository.ReviewResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReviewResultService {

    private final ReviewRepository reviewRepository;
    private final ReviewResultRepository reviewResultRepository;

    public ReviewResultService(
            ReviewRepository reviewRepository,
            ReviewResultRepository reviewResultRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewResultRepository = reviewResultRepository;
    }

    @Transactional
    public void saveResult(
            UUID reviewId,
            AIReviewResult aiResult
    ) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review not found: " + reviewId
                        )
                );

        ReviewResult reviewResult = new ReviewResult(
                review,
                aiResult.summary(),
                aiResult.score()
        );

        for (AIReviewResult.ReviewIssue issue : aiResult.issues()) {
            reviewResult.addIssue(
                    issue.severity().name(),
                    issue.category().name(),
                    issue.line(),
                    issue.message(),
                    issue.suggestion()
            );
        }

        reviewResultRepository.save(reviewResult);

        review.markCompleted();
    }
}
