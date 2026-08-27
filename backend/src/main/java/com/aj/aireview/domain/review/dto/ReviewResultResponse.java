package com.aj.aireview.domain.review.dto;

import com.aj.aireview.domain.review.entity.ReviewResult;

import java.util.List;
import java.util.UUID;

public record ReviewResultResponse(
        UUID id,
        String summary,
        int score,
        List<ReviewIssueResponse> issues
) {
    public static ReviewResultResponse from(ReviewResult result) {
        return new ReviewResultResponse(
                result.getId(),
                result.getSummary(),
                result.getScore(),
                result.getIssues()
                        .stream()
                        .map(ReviewIssueResponse::from)
                        .toList()
        );
    }
}
