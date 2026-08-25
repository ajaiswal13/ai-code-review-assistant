package com.aj.aireview.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
        @NotBlank(message = "Language is required")
        @Size(max = 50, message = "Language must not exceed 50 characters")
        String language,

        @NotBlank(message = "Code is required")
        @Size(max = 500_000, message = "Code must not exceed 500,000 characters")
        String code
) {
}
