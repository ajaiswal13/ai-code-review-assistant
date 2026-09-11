package com.aj.aireview.domain.knowledge.repository;

import com.aj.aireview.domain.knowledge.entity.KnowledgeChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnowledgeChunkRepository
        extends JpaRepository<KnowledgeChunk, Long> {

    List<KnowledgeChunk> findByDocumentIdOrderByChunkIndex(Long documentId);
}
