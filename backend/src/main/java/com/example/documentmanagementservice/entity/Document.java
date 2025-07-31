package com.example.documentmanagementservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Document {

    @Id
    private String uuid;

    private String fileName;
    private String fileType;
    private long fileSize;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Enumerated(EnumType.STRING)
    private DocumentSource source;

    private LocalDateTime createdAt;

    public Document(String fileName, String fileType, long fileSize, String filePath, DocumentSource source) {
        this.uuid = UUID.randomUUID().toString();
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.status = (source == DocumentSource.USER_UPLOADED) ? DocumentStatus.SUBMITTED : DocumentStatus.ACCEPTED;
        this.source = source;
        this.createdAt = LocalDateTime.now();
    }
}

