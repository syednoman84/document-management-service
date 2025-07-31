package com.example.virusscanservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentUploadedEvent {
    private String documentId;
    private String filePath;
}
