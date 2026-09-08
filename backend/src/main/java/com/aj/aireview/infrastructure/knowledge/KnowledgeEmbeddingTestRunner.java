package com.aj.aireview.infrastructure.knowledge;

import com.aj.aireview.domain.ai.EmbeddingService;
import com.aj.aireview.domain.knowledge.entity.KnowledgeChunk;
import com.aj.aireview.domain.knowledge.entity.KnowledgeDocument;
import com.aj.aireview.domain.knowledge.repository.KnowledgeChunkRepository;
import com.aj.aireview.domain.knowledge.repository.KnowledgeDocumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class KnowledgeEmbeddingTestRunner implements CommandLineRunner {

    private final EmbeddingService embeddingService;
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeChunkRepository chunkRepository;

    public KnowledgeEmbeddingTestRunner(
            EmbeddingService embeddingService,
            KnowledgeDocumentRepository documentRepository,
            KnowledgeChunkRepository chunkRepository
    ) {
        this.embeddingService = embeddingService;
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
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

        chunk.setEmbedding(chunkEmbedding);

        chunkRepository.save(chunk);

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

        // 7. Convert float[] → pgvector string
        String queryVector =
                Arrays.toString(queryEmbedding)
                        .replace(" ", "");

        // 8. Similarity search
        List<KnowledgeChunk> results =
                chunkRepository.findSimilarChunks(
                        queryVector,
                        5
                );

        // 9. Print retrieved knowledge
        System.out.println("Retrieved chunks:");

        for (KnowledgeChunk result : results) {
            System.out.println(
                    "- " + result.getContent()
            );
        }
    }
}
