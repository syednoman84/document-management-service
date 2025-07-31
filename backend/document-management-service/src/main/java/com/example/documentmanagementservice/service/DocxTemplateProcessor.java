package com.example.documentmanagementservice.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class DocxTemplateProcessor {

    public static byte[] fillDocxTemplate(byte[] templateData, Map<String, Object> parameters) throws Exception {
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(templateData))) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                paragraph.getRuns().forEach(run -> {
                    String text = run.getText(0);
                    if (text != null) {
                        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                            text = text.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
                        }
                        run.setText(text, 0);
                    }
                });
            }
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                document.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}

