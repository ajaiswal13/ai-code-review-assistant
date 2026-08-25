package com.aj.aireview.domain.review.controller;

import com.aj.aireview.domain.review.dto.CreateReviewRequest;
import com.aj.aireview.domain.review.dto.ReviewResponse;
import com.aj.aireview.domain.review.service.ReviewService;
import com.aj.aireview.security.user.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return reviewService.createReview(
                request,
                authenticatedUser
        );
    }

    @GetMapping
    public List<ReviewResponse> getReviews(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return reviewService.getReviews(authenticatedUser);
    }

    @GetMapping("/{reviewId}")
    public ReviewResponse getReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return reviewService.getReview(
                reviewId,
                authenticatedUser
        );
    }
}
