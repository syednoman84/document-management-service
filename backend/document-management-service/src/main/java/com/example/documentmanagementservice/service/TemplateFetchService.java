package com.example.documentmanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TemplateFetchService {

    @Value("${document.github.base-url}")
    private String baseUrl;

    @Value("${document.github.template-paths.html}")
    private String htmlPath;

    @Value("${document.github.template-paths.pdf}")
    private String pdfPath;

    @Value("${document.github.template-paths.docx}")
    private String docxPath;

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchTemplateContent(String templateName, String inputType) {
        String url;
        switch (inputType.toUpperCase()) {
            case "HTML":
                url = baseUrl + htmlPath + templateName;
                break;
            case "PDF":
                url = baseUrl + pdfPath + templateName;
                break;
            default:
                throw new IllegalArgumentException("Unsupported input type for text-based template: " + inputType);
        }
        System.out.println("Fetching text template from GitHub URL: " + url);
        return restTemplate.getForObject(url, String.class);
    }

    public byte[] fetchDocxTemplate(String templateName) {
        String url = baseUrl + docxPath + templateName;
        System.out.println("Fetching DOCX template from GitHub URL: " + url);
        return restTemplate.getForObject(url, byte[].class);
    }
}
