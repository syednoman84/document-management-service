package com.example.documentmanagementservice.controller;

import com.example.documentmanagementservice.dto.ErrorResponse;
import com.example.documentmanagementservice.dto.GenerateDocumentRequest;
import com.example.documentmanagementservice.dto.GenerateDocumentResponse;
import com.example.documentmanagementservice.entity.Document;
import com.example.documentmanagementservice.repository.DocumentRepository;
import com.example.documentmanagementservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentRepository documentRepository;

    @PostMapping("/generate")
    public ResponseEntity<?> generateDocument(@RequestBody GenerateDocumentRequest request) {
        try {
            GenerateDocumentResponse response = documentService.generateDocument(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Document generation failed", e.getMessage()));
        }
    }


    @GetMapping("/{uuid}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable String uuid) {
        Document doc = documentRepository.findById(uuid).orElse(null);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(doc.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(doc.getFileName())
                .build());

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (doc.getFileType().equalsIgnoreCase("PDF")) mediaType = MediaType.APPLICATION_PDF;
        if (doc.getFileType().equalsIgnoreCase("HTML")) mediaType = MediaType.TEXT_HTML;
        if (doc.getFileType().equalsIgnoreCase("DOCX")) mediaType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(doc.getFileSize())
                .contentType(mediaType)
                .body(resource);
    }
}

