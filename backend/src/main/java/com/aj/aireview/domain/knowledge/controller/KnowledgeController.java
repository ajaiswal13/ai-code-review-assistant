package com.aj.aireview.domain.knowledge.controller;

import com.aj.aireview.domain.knowledge.service.KnowledgeIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    private final KnowledgeIngestionService knowledgeIngestionService;

    public KnowledgeController(
            KnowledgeIngestionService knowledgeIngestionService
    ) {
        this.knowledgeIngestionService = knowledgeIngestionService;
    }

    @PostMapping("/documents")
    public ResponseEntity<Long> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category
    ) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("PDF file must not be empty");
        }

        String contentType = file.getContentType();

        if (!"application/pdf".equalsIgnoreCase(contentType)) {
            throw new IllegalArgumentException(
                    "Only PDF files are supported"
            );
        }

        Path tempFile = Files.createTempFile(
                "knowledge-upload-",
                ".pdf"
        );

        try {
            file.transferTo(tempFile);

            Long documentId =
                    knowledgeIngestionService.ingest(
                            tempFile,
                            title,
                            description,
                            category,
                            file.getOriginalFilename()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(documentId);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}