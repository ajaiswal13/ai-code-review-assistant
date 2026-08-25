package com.aj.aireview.domain.ai;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MockAIProvider implements AIProvider {

    @Override
    public AIReviewResult review(String language, String code) {

        return new AIReviewResult(
                "The code is generally good, but there are a few areas that can be improved.",
                75,
                List.of(
                        new AIReviewResult.ReviewIssue(
                                "HIGH",
                                "SECURITY",
                                10,
                                "Sensitive information may be exposed.",
                                "Avoid logging sensitive information."
                        ),
                        new AIReviewResult.ReviewIssue(
                                "MEDIUM",
                                "PERFORMANCE",
                                25,
                                "This code may perform unnecessary repeated processing.",
                                "Consider reducing repeated processing."
                        )
                )
        );
    }
}
