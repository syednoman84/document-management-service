package com.example.documentmanagementservice.service;

import com.example.documentmanagementservice.config.RabbitMQConfig;
import com.example.documentmanagementservice.dto.DocumentUploadedEvent;
import com.example.documentmanagementservice.dto.GenerateDocumentRequest;
import com.example.documentmanagementservice.dto.GenerateDocumentResponse;
import com.example.documentmanagementservice.entity.Document;
import com.example.documentmanagementservice.entity.DocumentSource;
import com.example.documentmanagementservice.entity.DocumentStatus;
import com.example.documentmanagementservice.repository.DocumentRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final TemplateFetchService templateFetchService;
    private final FileStorageService fileStorageService;
    private final DocumentRepository documentRepository;


    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        String templateName = request.getTemplateName();
        String templatePath = inputType.toLowerCase() + "/" + templateName;

        Document doc = new Document(
                fileName,
                outputType,
                finalContent.length,
                savedPath,
                DocumentSource.SYSTEM_GENERATED,
                templateName,
                templatePath
        );
        documentRepository.save(doc);

        return new GenerateDocumentResponse(
                doc.getUuid(),
                doc.getFileName(),
                doc.getFileType(),
                doc.getFileSize(),
                doc.getFilePath(),
                doc.getSource(),
                doc.getStatus(),
                doc.getCreatedAt(),
                doc.getTemplateName(),
                doc.getTemplatePath(),
                "/documents/" + doc.getUuid()
        );
    }

    public Document uploadUserDocument(MultipartFile file) {
        System.out.println("Uploaded file size in bytes: " + file.getSize());

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            String path = fileStorageService.save(bytes, fileName);

            Document doc = new Document();
            doc.setUuid(UUID.randomUUID().toString());
            doc.setFileName(fileName);
            doc.setFileType(file.getContentType());
            doc.setFileSize(file.getSize());
            doc.setFilePath(path);
            doc.setSource(DocumentSource.USER_UPLOADED);
            doc.setStatus(DocumentStatus.SUBMITTED);
            doc.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

            documentRepository.save(doc);
            emitDocumentUploadedEvent(doc);
            return doc;
        } catch (Exception e) { // <-- catch all exceptions
            log.error("❌ Failed to store uploaded document", e);
            throw new RuntimeException("Failed to store uploaded document", e);        }
    }

    public void emitDocumentUploadedEvent(Document doc) {
        DocumentUploadedEvent event = new DocumentUploadedEvent(doc.getUuid(), doc.getFilePath());
        rabbitTemplate.convertAndSend(RabbitMQConfig.DOCUMENT_UPLOAD_QUEUE, event);
    }


    @RabbitListener(queues = RabbitMQConfig.VIRUS_SCAN_RESULT_QUEUE)
    public void handleScanResult(Map<String, Object> result) {
        String documentId = (String) result.get("documentId");
        String status = (String) result.get("status");

        Document doc = documentRepository.findById(documentId).orElseThrow();
        doc.setStatus(DocumentStatus.valueOf(status));
        documentRepository.save(doc);
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
