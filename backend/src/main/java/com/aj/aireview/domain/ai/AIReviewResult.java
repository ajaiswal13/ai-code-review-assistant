package com.aj.aireview.domain.ai;

import com.aj.aireview.domain.review.entity.ReviewCategory;
import com.aj.aireview.domain.review.entity.ReviewSeverity;

import java.util.List;

public record AIReviewResult(
        String summary,
        int score,
        List<ReviewIssue> issues
) {

    public record ReviewIssue(
            ReviewSeverity severity,
            ReviewCategory category,
            Integer line,
            String message,
            String suggestion
    ) {
    }
}
