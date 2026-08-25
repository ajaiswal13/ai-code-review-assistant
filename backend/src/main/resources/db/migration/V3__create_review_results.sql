CREATE TABLE review_results (
                                id UUID PRIMARY KEY,
                                review_id UUID NOT NULL,
                                summary TEXT NOT NULL,
                                score INTEGER NOT NULL,
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                CONSTRAINT fk_review_results_review
                                    FOREIGN KEY (review_id)
                                        REFERENCES reviews(id),

                                CONSTRAINT uk_review_results_review_id
                                    UNIQUE (review_id)
);

CREATE TABLE review_issues (
                               id UUID PRIMARY KEY,
                               review_result_id UUID NOT NULL,
                               severity VARCHAR(20) NOT NULL,
                               category VARCHAR(50) NOT NULL,
                               line INTEGER,
                               message TEXT NOT NULL,
                               suggestion TEXT,

                               CONSTRAINT fk_review_issues_review_result
                                   FOREIGN KEY (review_result_id)
                                       REFERENCES review_results(id)
                                       ON DELETE CASCADE
);

CREATE INDEX idx_review_issues_review_result_id
    ON review_issues(review_result_id);