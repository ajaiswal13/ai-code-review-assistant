package com.aj.aireview.domain.knowledge.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "knowledge_chunk",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_knowledge_chunk_document_index",
                        columnNames = {"document_id", "chunk_index"}
                )
        }
)
public class KnowledgeChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "document_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_knowledge_chunk_document")
    )
    private KnowledgeDocument document;

    @Column(name = "chunk_index", nullable = false)
    private Integer chunkIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /*
     * pgvector column.
     *
     * We are not mapping this as a normal JPA numeric type yet.
     * We'll handle vector persistence explicitly when we build
     * the vector repository.
     */
    @Column(columnDefinition = "vector(1536)")
    private float[] embedding;

    @Column(nullable = false)
    private Instant createdAt;

    protected KnowledgeChunk() {
    }

    public KnowledgeChunk(
            KnowledgeDocument document,
            Integer chunkIndex,
            String content
    ) {
        this.document = document;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public KnowledgeDocument getDocument() {
        return document;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public String getContent() {
        return content;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }
}
