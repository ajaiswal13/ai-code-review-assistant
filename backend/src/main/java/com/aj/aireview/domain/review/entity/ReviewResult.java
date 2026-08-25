package com.aj.aireview.domain.review.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "review_results",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_review_results_review_id", columnNames = "review_id")
        }
)
public class ReviewResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false)
    private int score;

    @OneToMany(
            mappedBy = "reviewResult",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReviewIssue> issues = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected ReviewResult() {
    }

    public ReviewResult(
            Review review,
            String summary,
            int score
    ) {
        this.review = review;
        this.summary = summary;
        this.score = score;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void addIssue(
            String severity,
            String category,
            Integer line,
            String message,
            String suggestion
    ) {
        ReviewIssue issue = new ReviewIssue(
                this,
                severity,
                category,
                line,
                message,
                suggestion
        );

        issues.add(issue);
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Review getReview() {
        return review;
    }

    public String getSummary() {
        return summary;
    }

    public int getScore() {
        return score;
    }

    public List<ReviewIssue> getIssues() {
        return List.copyOf(issues);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
