package com.example.documentmanagementservice.dto;

import com.example.documentmanagementservice.entity.Document;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DocumentDto {
    private String id;
    private String fileName;
    private String fileType;
    private String status;
    private String source;
    private LocalDateTime createdAt;

    // constructor
    public DocumentDto(Document doc) {
        this.id = doc.getUuid();
        this.fileName = doc.getFileName();
        this.fileType = doc.getFileType();
        this.status = doc.getStatus().name();
        this.source = doc.getSource().name();
        this.createdAt = doc.getCreatedAt();
    }

}
