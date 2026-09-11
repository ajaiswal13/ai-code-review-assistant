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
            String code,
            String knowledgeContext
    ) {

        String systemPrompt = """
        You are an expert software engineer and code reviewer.

        Your task is to analyze the provided source code and identify
        genuine issues that should be addressed.

        Review the code for:

        - Security
        - Performance
        - Correctness
        - Maintainability
        - Code quality

        Rules:

        1. Analyze only the code provided by the user.
        2. Do not invent issues that are not supported by the code.
        3. Every issue must have a clear explanation.
        4. Every issue should include an actionable suggestion.
        5. The line number should correspond to the supplied source code.
        6. Score the code from 0 to 100, where 100 represents excellent
           code with no significant issues.
        7. Severity must be exactly one of:
           CRITICAL, HIGH, MEDIUM, LOW.
        8. Category must be exactly one of:
           SECURITY, PERFORMANCE, CORRECTNESS, MAINTAINABILITY, CODE_QUALITY.
        9. Return an empty issues list if no genuine issues are found.
        """;

        String userPrompt = """
        Review the following %s code.

        Relevant coding standards and guidelines:

        --- BEGIN GUIDELINES ---
        %s
        --- END GUIDELINES ---

        Apply these guidelines when they are relevant to the
        submitted code. Do not report an issue merely because a
        guideline exists; the issue must be supported by the code.

        --- BEGIN CODE ---
        ```%s
        %s
        ```
        --- END CODE ---
        """.formatted(
                language,
                knowledgeContext == null || knowledgeContext.isBlank()
                        ? "No relevant coding standards were found."
                        : knowledgeContext,
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