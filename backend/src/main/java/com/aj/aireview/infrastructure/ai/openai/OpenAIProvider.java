package com.aj.aireview.infrastructure.ai.openai;

import com.aj.aireview.domain.ai.AIProvider;
import com.aj.aireview.domain.ai.AIReviewResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class OpenAIProvider implements AIProvider {

    private final ChatClient chatClient;

    public OpenAIProvider(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public AIReviewResult review(
            String language,
            String code
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
                .call()
                .entity(AIReviewResult.class);
    }
}
