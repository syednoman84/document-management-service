package com.example.virusscanservice.listener;

import com.example.virusscanservice.dto.DocumentUploadedEvent;
import com.example.virusscanservice.dto.ScanResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class VirusScanListener {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.scanned}")
    private String scannedRoutingKey;

    @RabbitListener(queues = "${rabbitmq.upload-queue}")
    public void onDocumentUploaded(DocumentUploadedEvent event) {
        System.out.println("🔍 Received upload event: " + event);

        String scanResult = new Random().nextBoolean() ? "ACCEPTED" : "REJECTED";

        ScanResultEvent result = new ScanResultEvent(event.getDocumentId(), scanResult);

        rabbitTemplate.convertAndSend(exchange, scannedRoutingKey, result);
        System.out.println("📤 Scan result published: " + result);
    }
}
