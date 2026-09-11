package com.aj.aireview.infrastructure.knowledge;

import com.aj.aireview.domain.ai.EmbeddingService;
import com.aj.aireview.domain.knowledge.entity.KnowledgeChunk;
import com.aj.aireview.domain.knowledge.entity.KnowledgeDocument;
import com.aj.aireview.domain.knowledge.repository.KnowledgeChunkRepository;
import com.aj.aireview.domain.knowledge.repository.KnowledgeDocumentRepository;
import com.aj.aireview.domain.knowledge.repository.KnowledgeVectorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class KnowledgeEmbeddingTestRunner implements CommandLineRunner {

    private final EmbeddingService embeddingService;
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeVectorRepository vectorRepository;

    public KnowledgeEmbeddingTestRunner(
            EmbeddingService embeddingService,
            KnowledgeDocumentRepository documentRepository,
            KnowledgeChunkRepository chunkRepository,
            KnowledgeVectorRepository vectorRepository
    ) {
        this.embeddingService = embeddingService;
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.vectorRepository = vectorRepository;
    }

    @Override
    public void run(String... args) {

        // 1. Knowledge we want to store
        String content =
                "Controllers should not directly access repositories.";

        // 2. Generate embedding for the knowledge
        float[] chunkEmbedding =
                embeddingService.embed(content);

        System.out.println(
                "Chunk embedding dimensions: "
                        + chunkEmbedding.length
        );

        // 3. Create document
        KnowledgeDocument document = new KnowledgeDocument(
                "Coding Standards",
                "Coding standards for the AI Code Review Assistant.",
                "CODING_STANDARDS",
                "test"
        );

        documentRepository.save(document);

        // 4. Create chunk
        KnowledgeChunk chunk = new KnowledgeChunk(
                document,
                0,
                content
        );

        chunkRepository.save(chunk);

        vectorRepository.saveEmbedding(
                chunk.getId(),
                chunkEmbedding
        );

        // 5. Our user's/review's question
        String query =
                "Can a controller directly use a repository?";

        // 6. Generate query embedding
        float[] queryEmbedding =
                embeddingService.embed(query);

        System.out.println(
                "Query embedding dimensions: "
                        + queryEmbedding.length
        );

        List<Long> chunkIds =
                vectorRepository.findSimilarChunkIds(
                        queryEmbedding,
                        5
                );

        System.out.println("Retrieved chunk IDs:");

        for (Long chunkId : chunkIds) {
            System.out.println("- " + chunkId);
        }


    }
}
