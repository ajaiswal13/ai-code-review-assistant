package com.aj.aireview.infrastructure.ai.tools;

import com.aj.aireview.infrastructure.analysis.pmd.PmdAnalyzer;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class CodeAnalysisTool {

    private final PmdAnalyzer pmdAnalyzer;

    public CodeAnalysisTool(PmdAnalyzer pmdAnalyzer) {
        this.pmdAnalyzer = pmdAnalyzer;
    }

    @Tool(description = "Analyze Java source code for deterministic code quality findings")
    public CodeAnalysisResult analyzeCode(String code) {
        return pmdAnalyzer.analyze(code);
    }
}