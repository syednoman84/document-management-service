package com.example.virusscanservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanResultEvent {
    private String documentId;
    private String status; // "ACCEPTED" or "REJECTED"
}
