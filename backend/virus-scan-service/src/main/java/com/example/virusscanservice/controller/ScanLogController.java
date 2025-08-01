package com.example.virusscanservice.controller;

import com.example.virusscanservice.entity.ScanLog;
import com.example.virusscanservice.service.ScanLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scan-logs")
@RequiredArgsConstructor
public class ScanLogController {

    private final ScanLogService scanLogService;

    @GetMapping("/{documentId}")
    public List<ScanLog> getScanLogs(@PathVariable String documentId) {
        return scanLogService.getScanLogsByDocumentId(documentId);
    }

    @GetMapping("/all")
    public List<ScanLog> getAllScanLogs() {
        return scanLogService.getAllScanLogsSortedByDate();
    }

}
