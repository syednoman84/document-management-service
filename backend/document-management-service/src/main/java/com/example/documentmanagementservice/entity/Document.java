package com.example.documentmanagementservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Document {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String uuid;

    private String fileName;
    private String fileType;
    private long fileSize;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Enumerated(EnumType.STRING)
    private DocumentSource source;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_path")
    private String templatePath;

    private LocalDateTime createdAt;

    public Document(String fileName, String fileType, long fileSize, String filePath, DocumentSource source, String templateName, String templatePath) {
        this.uuid = UUID.randomUUID().toString();
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.status = (source == DocumentSource.USER_UPLOADED) ? DocumentStatus.SUBMITTED : DocumentStatus.ACCEPTED;
        this.source = source;
        this.templateName = templateName;
        this.templatePath = templatePath;
        this.createdAt = LocalDateTime.now();
    }
}

