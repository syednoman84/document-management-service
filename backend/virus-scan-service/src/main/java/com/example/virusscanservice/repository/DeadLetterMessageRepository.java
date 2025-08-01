package com.example.virusscanservice.repository;

import com.example.virusscanservice.entity.DeadLetterMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadLetterMessageRepository extends JpaRepository<DeadLetterMessage, Long> {
}

