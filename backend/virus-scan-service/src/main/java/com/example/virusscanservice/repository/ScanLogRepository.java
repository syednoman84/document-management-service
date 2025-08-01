package com.example.virusscanservice.repository;

import com.example.virusscanservice.entity.ScanLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanLogRepository extends JpaRepository<ScanLog, Long> {
    List<ScanLog> findByDocumentId(String documentId);
    List<ScanLog> findAllByOrderByScannedAtDesc();
}

