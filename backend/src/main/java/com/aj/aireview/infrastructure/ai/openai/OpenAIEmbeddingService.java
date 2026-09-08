package com.aj.aireview.infrastructure.ai.openai;

import com.aj.aireview.domain.ai.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIEmbeddingService implements EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public OpenAIEmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public float[] embed(String text) {
        return embeddingModel.embed(text);
    }
}