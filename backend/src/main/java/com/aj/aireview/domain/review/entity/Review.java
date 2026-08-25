package com.aj.aireview.domain.review.entity;

import com.aj.aireview.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String language;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReviewStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    public Review(
            User user,
            String language,
            String code
    ) {
        this.user = user;
        this.language = language;
        this.code = code;
        this.status = ReviewStatus.PENDING;
    }

    public void markProcessing() {
        if (this.status != ReviewStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending reviews can be processed"
            );
        }

        this.status = ReviewStatus.PROCESSING;
    }

    public void markCompleted() {
        if (this.status != ReviewStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only processing reviews can be completed"
            );
        }

        this.status = ReviewStatus.COMPLETED;
    }

    public void markFailed() {
        if (this.status != ReviewStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only processing reviews can be marked as failed"
            );
        }

        this.status = ReviewStatus.FAILED;
    }
}