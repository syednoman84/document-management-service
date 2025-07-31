package com.example.documentmanagementservice.service;

import org.docx4j.fonts.Mapper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.Docx4J;
import java.io.*;

public class DocxToPdfConverter {

    public static byte[] convertDocxToPdf(byte[] docxData) throws Exception {
        try (InputStream inputStream = new ByteArrayInputStream(docxData);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);

            // Register system fonts
            Mapper fontMapper = new org.docx4j.fonts.IdentityPlusMapper();
            wordMLPackage.setFontMapper(fontMapper);

            Docx4J.toPDF(wordMLPackage, outputStream);

            return outputStream.toByteArray();
        }
    }
}
