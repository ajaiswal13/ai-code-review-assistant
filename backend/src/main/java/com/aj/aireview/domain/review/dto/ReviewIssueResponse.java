package com.aj.aireview.domain.review.dto;

import com.aj.aireview.domain.review.entity.ReviewIssue;

import java.util.UUID;

public record ReviewIssueResponse(
        UUID id,
        String severity,
        String category,
        Integer line,
        String message,
        String suggestion
) {
    public static ReviewIssueResponse from(ReviewIssue issue) {
        return new ReviewIssueResponse(
                issue.getId(),
                issue.getSeverity(),
                issue.getCategory(),
                issue.getLine(),
                issue.getMessage(),
                issue.getSuggestion()
        );
    }
}
