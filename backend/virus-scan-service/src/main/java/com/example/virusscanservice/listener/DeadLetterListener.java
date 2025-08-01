package com.example.virusscanservice.listener;

import com.example.virusscanservice.entity.DeadLetterMessage;
import com.example.virusscanservice.repository.DeadLetterMessageRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class DeadLetterListener {

    private final DeadLetterMessageRepository repository;

    public DeadLetterListener(DeadLetterMessageRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "document.dlq")
    public void handleDeadLetter(Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        String reason = message.getMessageProperties()
                .getHeaders()
                .getOrDefault("x-death", "unknown")
                .toString();

        DeadLetterMessage dead = new DeadLetterMessage();
        dead.setPayload(body);
        dead.setOriginalQueue("document.upload.queue");
        dead.setReason(reason);
        dead.setReceivedAt(LocalDateTime.now());

        repository.save(dead);

        System.err.println("💀 Persisted message from DLQ: " + body);
    }
}
