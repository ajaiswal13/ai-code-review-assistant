package com.aj.aireview.infrastructure.knowledge.document;

import com.aj.aireview.domain.knowledge.service.DocumentTextExtractor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class PdfDocumentTextExtractor implements DocumentTextExtractor {

    public String extractText(Path pdfPath) {

        try (PDDocument document = Loader.loadPDF(pdfPath.toFile())) {

            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setSortByPosition(true);

            return textStripper.getText(document);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to extract text from PDF: " + pdfPath,
                    e
            );
        }
    }
}
