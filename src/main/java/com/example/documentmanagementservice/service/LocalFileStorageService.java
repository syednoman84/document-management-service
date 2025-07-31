package com.example.documentmanagementservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LocalFileStorageService implements FileStorageService {

    private final String rootDir;

    public LocalFileStorageService(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public String save(byte[] fileData, String fileName) throws IOException {
        File dir = new File(rootDir);
        if (!dir.exists()) dir.mkdirs();

        String filePath = rootDir + File.separator + fileName;
        Files.write(Paths.get(filePath), fileData);
        return filePath;
    }
}

