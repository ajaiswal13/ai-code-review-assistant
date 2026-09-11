package com.aj.aireview.domain.knowledge.service;

import java.nio.file.Path;

public interface DocumentTextExtractor {
    String extractText(Path documentPath);
}
