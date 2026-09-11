package com.aj.aireview.domain.knowledge.service;

import com.aj.aireview.domain.ai.EmbeddingService;
import com.aj.aireview.domain.knowledge.entity.KnowledgeChunk;
import com.aj.aireview.domain.knowledge.repository.KnowledgeChunkRepository;
import com.aj.aireview.domain.knowledge.repository.KnowledgeVectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeRetrievalService {

    private static final int TOP_K = 3;

    private final EmbeddingService embeddingService;
    private final KnowledgeVectorRepository vectorRepository;
    private final KnowledgeChunkRepository chunkRepository;

    public KnowledgeRetrievalService(
            EmbeddingService embeddingService,
            KnowledgeVectorRepository vectorRepository,
            KnowledgeChunkRepository chunkRepository
    ) {
        this.embeddingService = embeddingService;
        this.vectorRepository = vectorRepository;
        this.chunkRepository = chunkRepository;
    }

    public String retrieveRelevantGuidelines(String code) {

        float[] queryEmbedding = embeddingService.embed(code);

        List<Long> chunkIds =
                vectorRepository.findSimilarChunkIds(
                        queryEmbedding,
                        TOP_K
                );

        if (chunkIds.isEmpty()) {
            return "";
        }

        return chunkIds.stream()
                .map(chunkRepository::findById)
                .flatMap(java.util.Optional::stream)
                .map(KnowledgeChunk::getContent)
                .reduce(
                        "",
                        (context, content) ->
                                context.isEmpty()
                                        ? content
                                        : context + "\n\n" + content
                );
    }
}
