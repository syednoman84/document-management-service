package com.example.documentmanagementservice.config;

import com.example.documentmanagementservice.service.FileStorageService;
import com.example.documentmanagementservice.service.LocalFileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Value("${document.storage.type}")
    private String storageType;

    @Value("${document.storage.local-path}")
    private String localPath;

    @Bean
    public FileStorageService fileStorageService() {
        switch (storageType.toLowerCase()) {
            case "local":
                return new LocalFileStorageService(localPath);
//            case "sftp":
//                return new SftpFileStorageService(); // TODO
//            case "s3":
//                return new S3FileStorageService();   // TODO
            default:
                throw new IllegalArgumentException("Unsupported storage type: " + storageType);
        }
    }
}
