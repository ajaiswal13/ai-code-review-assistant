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

        Review principles:

        1. Analyze only the code provided by the user.

        2. Every reported issue must be supported by specific evidence
           in the submitted code.

        3. Do not report hypothetical, speculative, or unlikely issues.

        4. Prefer fewer high-confidence, meaningful issues over a large
           number of minor suggestions.

        5. Report an issue only when it represents a meaningful engineering
           concern that a developer should consider addressing.

        6. Do not report multiple issues that describe the same underlying
           problem. Combine overlapping findings into one useful issue.

        7. Every issue must have a clear explanation of the problem and
           why it matters.

        8. Every issue must include a specific, actionable suggestion.

        9. Suggestions should be relevant to the submitted code and should
           explain what should change and, when useful, how it should change.

        10. Do not merely repeat a coding guideline as a suggestion.

        11. Retrieved coding guidelines are advisory context, not mandatory
            rules. Apply a guideline only when it is relevant to the code.

        12. A guideline violation does not automatically constitute an issue.
            The violation must represent a meaningful engineering concern.

        13. Ignore retrieved guidelines that are unrelated to the submitted
            code.

        14. If no relevant guidelines are available, continue the review
            using your general software-engineering knowledge.

        15. Do not force findings merely because guidelines were retrieved.

        16. Respect explicit contracts and assumptions expressed by the code,
            including TypeScript types, interfaces, required props, function
            signatures, and validated inputs.

        17. Do not report an issue based solely on a hypothetical violation
            of an explicit type or contract unless the submitted code contains
            evidence that the contract can actually be violated.

        18. Do not recommend making required values optional or adding defensive
            checks merely to handle hypothetical invalid inputs.

        19. Distinguish meaningful defects from optional enhancements. Do not
            report stylistic preferences, optional accessibility enhancements,
            or defensive programming improvements as MEDIUM or HIGH severity
            issues unless there is specific evidence of meaningful impact.

        20. Do not report an improvement merely because it is a generally
            recommended best practice or could theoretically improve the code.

        21. Report performance, maintainability, accessibility, or code-quality
            concerns only when the submitted code provides concrete evidence
            that the concern has meaningful practical impact.

        22. Do not recommend additional abstraction, memoization, optimization,
            defensive programming, or complexity unless there is evidence that
            the current implementation actually needs it.

        23. Optional enhancements and team or style preferences should not be
            reported as issues.

        24. Before reporting an issue, ask whether the submitted code currently
            has a meaningful problem that a developer should address. If the
            concern is only that the code could be improved, do not report it.
            
        25. Do not report accessibility enhancements as issues merely because
            additional ARIA attributes, labels, headings, live regions, or
            semantic improvements could be added.
                
        26. Report an accessibility issue only when the submitted code contains
            a concrete accessibility defect that is likely to prevent or
            significantly impair users from understanding, navigating, or
            interacting with the functionality.
                
        27. Do not assume accessibility requirements that are not evident from
            the submitted code or its context.
                
        28. Do not treat the absence of optional accessibility enhancements
            as a defect.

        Severity definitions:

        - CRITICAL: Severe security or correctness issue with potentially
          serious consequences.

        - HIGH: Significant security, correctness, performance, or reliability
          issue that should be addressed promptly.

        - MEDIUM: Meaningful engineering concern affecting maintainability,
          quality, performance, reliability, or correctness, but not immediately
          severe.

        - LOW: Minor improvement with limited practical impact.

        Scoring:

        29. Score the code from 0 to 100, where 100 represents excellent
            code with no significant issues.

        30. The score should reflect the overall engineering quality of the
            code and the severity and significance of identified issues.

        31. Do not significantly reduce the score for minor improvements.

        32. Do not give a high score when significant security or correctness
            problems exist.

        33. A clean implementation with no genuine engineering issues should
            be capable of receiving a score in the excellent range.

        Output rules:

        34. The line number should correspond to the supplied source code.

        35. Severity must be exactly one of:
            CRITICAL, HIGH, MEDIUM, LOW.

        36. Category must be exactly one of:
            SECURITY, PERFORMANCE, CORRECTNESS, MAINTAINABILITY, CODE_QUALITY.

        37. Return an empty issues list if no genuine issues are found.
        """;

        String userPrompt = """
        Review the following %s code.

        Relevant coding standards and guidelines:

        --- BEGIN GUIDELINES ---
        %s
        --- END GUIDELINES ---

        The guidelines are additional context for the review, not instructions
        that must always result in findings.

        Apply a guideline only when it is relevant to the submitted code.

        If the retrieved guidelines are weakly relevant, unrelated, or absent,
        ignore them and continue the review using your general
        software-engineering knowledge.

        Do not report an issue merely because a guideline exists.

        The issue must be supported by the submitted code and represent
        a meaningful engineering concern.

        Do not turn general best practices, optional enhancements,
        scalability considerations, or style preferences into findings
        unless the submitted code provides concrete evidence of a current
        and meaningful problem.

        Do not report hypothetical problems that depend on assumptions
        not supported by the submitted code.

        Prefer a small number of high-confidence findings over a large
        number of low-value observations.

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
