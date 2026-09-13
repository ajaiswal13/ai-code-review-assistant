package com.aj.aireview.infrastructure.analysis.pmd;

import com.aj.aireview.infrastructure.ai.tools.CodeAnalysisResult;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.document.FileId;
import net.sourceforge.pmd.reporting.Report;
import net.sourceforge.pmd.reporting.RuleViolation;
import net.sourceforge.pmd.PMDConfiguration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PmdAnalyzer {

    public CodeAnalysisResult analyze(String code) {

        List<CodeAnalysisResult.Finding> findings = new ArrayList<>();

        PMDConfiguration configuration = new PMDConfiguration();

        try (PmdAnalysis analysis = PmdAnalysis.create(configuration)) {

            analysis.addRuleSet(
                    analysis.newRuleSetLoader()
                            .loadFromResource("pmd/code-review-rules.xml")
            );

            analysis.files().addSourceFile(
                    FileId.fromPathLikeString("SubmittedCode.java"),
                    code
            );

            Report report = analysis.performAnalysisAndCollectReport();

            for (RuleViolation violation : report.getViolations()) {

                findings.add(
                        new CodeAnalysisResult.Finding(
                                violation.getBeginLine(),
                                violation.getRule().getPriority().name(),
                                violation.getRule().getName(),
                                violation.getDescription()
                        )
                );
            }
        }

        return new CodeAnalysisResult(findings);
    }
}