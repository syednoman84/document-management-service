package com.example.virusscanservice.service;

import com.example.virusscanservice.entity.ScanLog;
import com.example.virusscanservice.repository.ScanLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScanLogService {

    private final ScanLogRepository scanLogRepository;

    public List<ScanLog> getScanLogsByDocumentId(String documentId) {
        return scanLogRepository.findByDocumentId(documentId);
    }

    public List<ScanLog> getAllScanLogsSortedByDate() {
        return scanLogRepository.findAllByOrderByScannedAtDesc();
    }

}

