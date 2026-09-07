package com.aj.aireview.infrastructure.sse;

import com.aj.aireview.domain.review.entity.ReviewStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReviewSseManager {

    private final Map<UUID, Set<SseEmitter>> emitters =
            new ConcurrentHashMap<>();

    public SseEmitter subscribe(UUID reviewId) {
        SseEmitter emitter = new SseEmitter(0L);

        emitters
                .computeIfAbsent(
                        reviewId,
                        id -> ConcurrentHashMap.newKeySet()
                )
                .add(emitter);

        emitter.onCompletion(() -> removeEmitter(reviewId, emitter));
        emitter.onTimeout(() -> removeEmitter(reviewId, emitter));
        emitter.onError(error -> removeEmitter(reviewId, emitter));

        return emitter;
    }

    public void publishStatus(
            UUID reviewId,
            ReviewStatus status
    ) {
        Set<SseEmitter> reviewEmitters = emitters.get(reviewId);

        if (reviewEmitters == null) {
            return;
        }

        for (SseEmitter emitter : reviewEmitters) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("REVIEW_STATUS")
                                .data(Map.of(
                                        "status",
                                        status
                                ))
                );

                if (status == ReviewStatus.COMPLETED
                        || status == ReviewStatus.FAILED) {
                    emitter.complete();
                }

            } catch (IOException | IllegalStateException exception) {
                removeEmitter(reviewId, emitter);
            }
        }
    }

    private void removeEmitter(
            UUID reviewId,
            SseEmitter emitter
    ) {
        Set<SseEmitter> reviewEmitters = emitters.get(reviewId);

        if (reviewEmitters == null) {
            return;
        }

        reviewEmitters.remove(emitter);

        if (reviewEmitters.isEmpty()) {
            emitters.remove(reviewId);
        }
    }
}
