package com.aj.aireview.domain.review.event;

import java.util.UUID;

public record ReviewCreatedEvent(UUID reviewId) {
}
