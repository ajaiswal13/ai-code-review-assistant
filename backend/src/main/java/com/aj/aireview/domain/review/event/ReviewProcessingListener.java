package com.aj.aireview.domain.review.event;

import com.aj.aireview.domain.review.service.ReviewProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReviewProcessingListener {

    private final ReviewProcessor reviewProcessor;

    public ReviewProcessingListener(ReviewProcessor reviewProcessor) {
        this.reviewProcessor = reviewProcessor;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReviewCreated(ReviewCreatedEvent event) {
        reviewProcessor.process(event.reviewId());
    }
}
