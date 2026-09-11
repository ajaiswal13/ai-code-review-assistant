package com.aj.aireview.domain.review.service;

import com.aj.aireview.domain.ai.AIProvider;
import com.aj.aireview.domain.ai.AIReviewResult;
import com.aj.aireview.domain.knowledge.service.KnowledgeRetrievalService;
import com.aj.aireview.domain.review.entity.Review;
import com.aj.aireview.domain.review.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(ReviewProcessor.class);

    private final ReviewRepository reviewRepository;
    private final ReviewStatusService reviewStatusService;
    private final ReviewResultService reviewResultService;
    private final AIProvider aiProvider;
    private final KnowledgeRetrievalService knowledgeRetrievalService;

    public ReviewProcessor(
            ReviewRepository reviewRepository,
            ReviewStatusService reviewStatusService,
            ReviewResultService reviewResultService,
            AIProvider aiProvider,
            KnowledgeRetrievalService knowledgeRetrievalService
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewStatusService = reviewStatusService;
        this.reviewResultService = reviewResultService;
        this.aiProvider = aiProvider;
        this.knowledgeRetrievalService = knowledgeRetrievalService;
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

            String knowledgeContext =
                    knowledgeRetrievalService.retrieveRelevantGuidelines(
                            review.getCode()
                    );

            AIReviewResult aiResult = aiProvider.review(
                    review.getLanguage(),
                    review.getCode(),
                    knowledgeContext
            );

            reviewResultService.saveResult(
                    reviewId,
                    aiResult
            );

        } catch (Exception exception) {

            log.error(
                    "Review processing failed. reviewId={}",
                    reviewId,
                    exception
            );

            reviewStatusService.markFailed(reviewId);

        }
    }
}
