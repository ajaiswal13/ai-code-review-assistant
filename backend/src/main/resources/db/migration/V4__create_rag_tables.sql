CREATE TABLE knowledge_document (
                                    id BIGSERIAL PRIMARY KEY,
                                    title VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    category VARCHAR(100) NOT NULL,
                                    source VARCHAR(500),
                                    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE knowledge_chunk (
                                 id BIGSERIAL PRIMARY KEY,
                                 document_id BIGINT NOT NULL,
                                 chunk_index INTEGER NOT NULL,
                                 content TEXT NOT NULL,
                                 embedding VECTOR(1536),
                                 created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_knowledge_chunk_document
                                     FOREIGN KEY (document_id)
                                         REFERENCES knowledge_document(id)
                                         ON DELETE CASCADE,

                                 CONSTRAINT uk_knowledge_chunk_document_index
                                     UNIQUE (document_id, chunk_index)
);

CREATE INDEX idx_knowledge_chunk_document_id
    ON knowledge_chunk(document_id);