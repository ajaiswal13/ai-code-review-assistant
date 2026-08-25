package com.aj.aireview.domain.ai;

public interface AIProvider {
    AIReviewResult review(
            String language,
            String code
    );
}
