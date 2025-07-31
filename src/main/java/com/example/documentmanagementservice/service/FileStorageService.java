package com.example.documentmanagementservice.service;

public interface FileStorageService {
    String save(byte[] fileData, String fileName) throws Exception;
}

