package com.aj.aireview.domain.ai;

import java.util.List;

public record AIReviewResult(
        String summary,
        int score,
        List<ReviewIssue> issues
) {

    public record ReviewIssue(
            String severity,
            String category,
            Integer line,
            String message,
            String suggestion
    ) {
    }
}
