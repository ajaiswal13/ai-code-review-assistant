package com.aj.aireview.domain.knowledge.repository;

import com.aj.aireview.domain.knowledge.entity.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeDocumentRepository
        extends JpaRepository<KnowledgeDocument, Long> {
}
