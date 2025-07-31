package com.example.documentmanagementservice.service;

import com.example.documentmanagementservice.dto.GenerateDocumentRequest;
import com.example.documentmanagementservice.dto.GenerateDocumentResponse;
import com.example.documentmanagementservice.entity.Document;
import com.example.documentmanagementservice.entity.DocumentSource;
import com.example.documentmanagementservice.repository.DocumentRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final TemplateFetchService templateFetchService;
    private final FileStorageService fileStorageService;
    private final DocumentRepository documentRepository;

    public GenerateDocumentResponse generateDocument(GenerateDocumentRequest request) throws Exception {
        String inputType = request.getInputType().toUpperCase();
        String outputType = request.getOutputType().toUpperCase();

        String fileName = UUID.randomUUID() + "." + outputType.toLowerCase();
        byte[] finalContent;

        switch (inputType) {
            case "HTML":
            case "PDF": {
                // Fetch HTML content from html/ or pdf/ folder
                String template = templateFetchService.fetchTemplateContent(request.getTemplateName(), inputType);
                String filled = fillPlaceholders(template, request.getParameters());

                switch (outputType) {
                    case "HTML":
                        finalContent = filled.getBytes(StandardCharsets.UTF_8);
                        break;
                    case "PDF":
                        finalContent = renderHtmlToPdf(filled);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported outputType for HTML/PDF input: " + outputType);
                }
                break;
            }

            case "DOCX": {
                byte[] docxTemplate = templateFetchService.fetchDocxTemplate(request.getTemplateName());
                byte[] filledDocx = DocxTemplateProcessor.fillDocxTemplate(docxTemplate, request.getParameters());

                if (outputType.equals("DOCX")) {
                    finalContent = filledDocx;
                } else if (outputType.equals("PDF")) {
                    finalContent = DocxToPdfConverter.convertDocxToPdf(filledDocx);
                } else {
                    throw new IllegalArgumentException("Unsupported outputType for DOCX input: " + outputType);
                }
                break;
            }

            default:
                throw new IllegalArgumentException("Unsupported inputType: " + inputType);
        }

        String savedPath = fileStorageService.save(finalContent, fileName);

        Document doc = new Document(
                fileName,
                outputType,
                finalContent.length,
                savedPath,
                DocumentSource.SYSTEM_GENERATED
        );
        documentRepository.save(doc);

        return new GenerateDocumentResponse(doc.getUuid(), "/documents/" + doc.getUuid());
    }

    private String fillPlaceholders(String template, Map<String, Object> params) {
        String result = template;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return result;
    }

    private byte[] renderHtmlToPdf(String html) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        }
    }
}
