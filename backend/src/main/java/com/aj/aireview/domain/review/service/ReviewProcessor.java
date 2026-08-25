package com.aj.aireview.domain.review.service;

import com.aj.aireview.domain.ai.AIProvider;
import com.aj.aireview.domain.ai.AIReviewResult;
import com.aj.aireview.domain.review.entity.Review;
import com.aj.aireview.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewProcessor {

    private final ReviewRepository reviewRepository;
    private final ReviewStatusService reviewStatusService;
    private final ReviewResultService reviewResultService;
    private final AIProvider aiProvider;

    public ReviewProcessor(
            ReviewRepository reviewRepository,
            ReviewStatusService reviewStatusService,
            ReviewResultService reviewResultService,
            AIProvider aiProvider
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewStatusService = reviewStatusService;
        this.reviewResultService = reviewResultService;
        this.aiProvider = aiProvider;
    }

    public void process(UUID reviewId) {

        reviewStatusService.markProcessing(reviewId);

        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Review not found: " + reviewId
                            )
                    );

            AIReviewResult aiResult = aiProvider.review(
                    review.getLanguage(),
                    review.getCode()
            );

            reviewResultService.saveResult(
                    reviewId,
                    aiResult
            );

        } catch (Exception exception) {

            reviewStatusService.markFailed(reviewId);

            throw exception;
        }
    }
}
