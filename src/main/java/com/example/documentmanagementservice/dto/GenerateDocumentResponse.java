package com.example.documentmanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerateDocumentResponse {
    private String uuid;
    private String downloadUrl;
}

