package com.aj.aireview.infrastructure.ai.openai;

import com.aj.aireview.domain.ai.AIProvider;
import com.aj.aireview.domain.ai.AIReviewResult;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import java.time.Duration;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;

@Component
public class OpenAIProvider implements AIProvider {

    private final ChatClient chatClient;
    private final Duration timeout;

    public OpenAIProvider(
            ChatClient.Builder chatClientBuilder,
            @Value("${ai.openai.timeout}") Duration timeout
    ) {
        this.chatClient = chatClientBuilder.build();
        this.timeout = timeout;
    }

    @Retry(name = "aiProvider")
    @Override
    public AIReviewResult review(
            String language,
            String code
    ) {

        String systemPrompt = """
        ...
        """;

        String userPrompt = """
                Review the following %s code:

                ```%s
                %s
                ```
                """.formatted(
                language,
                language,
                code
        );

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .options(OpenAiChatOptions.builder()
                        .timeout(timeout)
                        .maxRetries(0))
                .call()
                .entity(AIReviewResult.class);
    }
}