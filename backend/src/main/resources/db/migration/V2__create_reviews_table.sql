CREATE TABLE reviews (
                         id UUID PRIMARY KEY,
                         user_id UUID NOT NULL,
                         language VARCHAR(50) NOT NULL,
                         code TEXT NOT NULL,
                         status VARCHAR(20) NOT NULL,
                         created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

                         CONSTRAINT fk_reviews_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id)
);

CREATE INDEX idx_reviews_user_id
    ON reviews(user_id);