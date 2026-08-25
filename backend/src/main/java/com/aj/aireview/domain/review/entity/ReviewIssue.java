package com.aj.aireview.domain.review.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "review_issues")
public class ReviewIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_result_id", nullable = false)
    private ReviewResult reviewResult;

    @Column(nullable = false, length = 20)
    private String severity;

    @Column(nullable = false, length = 50)
    private String category;

    private Integer line;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    protected ReviewIssue() {
    }

    public ReviewIssue(
            ReviewResult reviewResult,
            String severity,
            String category,
            Integer line,
            String message,
            String suggestion
    ) {
        this.reviewResult = reviewResult;
        this.severity = severity;
        this.category = category;
        this.line = line;
        this.message = message;
        this.suggestion = suggestion;
    }

    public java.util.UUID getId() {
        return id;
    }

    public ReviewResult getReviewResult() {
        return reviewResult;
    }

    public String getSeverity() {
        return severity;
    }

    public String getCategory() {
        return category;
    }

    public Integer getLine() {
        return line;
    }

    public String getMessage() {
        return message;
    }

    public String getSuggestion() {
        return suggestion;
    }
}