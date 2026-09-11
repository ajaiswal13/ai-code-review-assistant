package com.aj.aireview.domain.knowledge.repository;

import java.util.List;

public interface KnowledgeVectorRepository {
    void saveEmbedding(Long chunkId, float[] embedding);

    List<Long> findSimilarChunkIds(float[] queryEmbedding, int limit);
}
