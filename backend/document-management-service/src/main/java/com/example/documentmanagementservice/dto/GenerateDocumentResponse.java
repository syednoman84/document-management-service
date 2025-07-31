package com.example.documentmanagementservice.dto;

import com.example.documentmanagementservice.entity.DocumentSource;
import com.example.documentmanagementservice.entity.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GenerateDocumentResponse {
    private String uuid;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String filePath;
    private DocumentSource source;
    private DocumentStatus status;
    private LocalDateTime createdAt;
    private String templateName;
    private String templatePath;
    private String downloadUrl;
}
