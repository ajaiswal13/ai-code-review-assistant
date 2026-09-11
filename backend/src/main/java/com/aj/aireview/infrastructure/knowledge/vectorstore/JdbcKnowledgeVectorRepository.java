package com.aj.aireview.infrastructure.knowledge.vectorstore;

import com.aj.aireview.domain.knowledge.repository.KnowledgeVectorRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class JdbcKnowledgeVectorRepository implements KnowledgeVectorRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcKnowledgeVectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveEmbedding(Long chunkId, float[] embedding) {

        String vector =
                Arrays.toString(embedding)
                        .replace(" ", "");

        jdbcTemplate.update(
                """
                UPDATE knowledge_chunk
                SET embedding = CAST(? AS vector)
                WHERE id = ?
                """,
                vector,
                chunkId
        );
    }

    @Override
    public List<Long> findSimilarChunkIds(
            float[] queryEmbedding,
            int limit
    ) {

        String vector =
                Arrays.toString(queryEmbedding)
                        .replace(" ", "");

        return jdbcTemplate.query(
                """
                SELECT id
                FROM knowledge_chunk
                WHERE embedding IS NOT NULL
                ORDER BY embedding <=> CAST(? AS vector)
                LIMIT ?
                """,
                (resultSet, rowNum) ->
                        resultSet.getLong("id"),
                vector,
                limit
        );
    }
}
