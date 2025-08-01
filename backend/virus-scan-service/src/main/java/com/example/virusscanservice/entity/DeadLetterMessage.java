package com.example.virusscanservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class DeadLetterMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalQueue;

    private String reason;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private LocalDateTime receivedAt;

}
