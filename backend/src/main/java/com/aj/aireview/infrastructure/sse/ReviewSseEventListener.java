package com.aj.aireview.infrastructure.sse;

import com.aj.aireview.application.review.event.ReviewStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReviewSseEventListener {

    private final ReviewSseManager reviewSseManager;

    public ReviewSseEventListener(
            ReviewSseManager reviewSseManager
    ) {
        this.reviewSseManager = reviewSseManager;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReviewStatusChanged(
            ReviewStatusChangedEvent event
    ) {
        reviewSseManager.publishStatus(
                event.reviewId(),
                event.status()
        );
    }
}
