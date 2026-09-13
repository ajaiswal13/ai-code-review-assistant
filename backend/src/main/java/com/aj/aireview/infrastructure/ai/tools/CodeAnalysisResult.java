package com.aj.aireview.infrastructure.ai.tools;

import java.util.List;

public record CodeAnalysisResult(
        List<Finding> findings
) {

    public record Finding(
            int line,
            String severity,
            String rule,
            String message
    ) {
    }
}
