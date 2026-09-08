package com.aj.aireview.domain.knowledge.repository;

import com.aj.aireview.domain.knowledge.entity.KnowledgeChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KnowledgeChunkRepository
        extends JpaRepository<KnowledgeChunk, Long> {

    List<KnowledgeChunk> findByDocumentIdOrderByChunkIndex(Long documentId);

    @Query(value = """
        SELECT *
        FROM knowledge_chunk
        ORDER BY embedding <=> CAST(:queryVector AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<KnowledgeChunk> findSimilarChunks(
            @Param("queryVector") String queryVector,
            @Param("limit") int limit
    );
}
