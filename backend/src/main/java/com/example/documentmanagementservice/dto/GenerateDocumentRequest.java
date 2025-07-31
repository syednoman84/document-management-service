package com.example.documentmanagementservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class GenerateDocumentRequest {
    private String templateName;
    private String templateType; // HTML, DOCX
    private Map<String, Object> parameters;
    private String inputType;   // HTML, PDF, DOCX
    private String outputType;  // HTML, PDF, DOCX

}
