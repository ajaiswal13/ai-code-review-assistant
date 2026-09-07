package com.aj.aireview.application.review.event;
import com.aj.aireview.domain.review.entity.ReviewStatus;

import java.util.UUID;

public record ReviewStatusChangedEvent(
        UUID reviewId,
        ReviewStatus status
) {
}