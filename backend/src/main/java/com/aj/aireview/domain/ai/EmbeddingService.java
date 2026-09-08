package com.aj.aireview.domain.ai;

public interface EmbeddingService {

    float[] embed(String text);
}