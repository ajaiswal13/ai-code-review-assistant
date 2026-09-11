package com.aj.aireview.domain.knowledge.service;

import com.aj.aireview.domain.ai.EmbeddingService;
import com.aj.aireview.domain.knowledge.entity.KnowledgeChunk;
import com.aj.aireview.domain.knowledge.entity.KnowledgeDocument;
import com.aj.aireview.domain.knowledge.repository.KnowledgeChunkRepository;
import com.aj.aireview.domain.knowledge.repository.KnowledgeDocumentRepository;
import com.aj.aireview.domain.knowledge.repository.KnowledgeVectorRepository;
import com.aj.aireview.domain.knowledge.service.DocumentTextExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;

@Service
public class KnowledgeIngestionService {

    private final DocumentTextExtractor documentTextExtractor;
    private final TextChunker textChunker;
    private final EmbeddingService embeddingService;
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeVectorRepository vectorRepository;

    public KnowledgeIngestionService(
            DocumentTextExtractor documentTextExtractor,
            TextChunker textChunker,
            EmbeddingService embeddingService,
            KnowledgeDocumentRepository documentRepository,
            KnowledgeChunkRepository chunkRepository,
            KnowledgeVectorRepository vectorRepository
    ) {
        this.documentTextExtractor = documentTextExtractor;
        this.textChunker = textChunker;
        this.embeddingService = embeddingService;
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.vectorRepository = vectorRepository;
    }

    @Transactional
    public Long ingest(
            Path pdfPath,
            String title,
            String description,
            String category,
            String source
    ) {

        String text = documentTextExtractor.extractText(pdfPath);

        List<String> chunks = textChunker.chunk(text);

        if (chunks.isEmpty()) {
            throw new IllegalArgumentException(
                    "PDF does not contain extractable text"
            );
        }

        KnowledgeDocument document =
                new KnowledgeDocument(
                        title,
                        description,
                        category,
                        source
                );

        documentRepository.save(document);

        for (int i = 0; i < chunks.size(); i++) {

            String content = chunks.get(i);

            KnowledgeChunk chunk =
                    new KnowledgeChunk(
                            document,
                            i,
                            content
                    );

            chunkRepository.save(chunk);

            float[] embedding =
                    embeddingService.embed(content);

            vectorRepository.saveEmbedding(
                    chunk.getId(),
                    embedding
            );
        }

        return document.getId();
    }
}
