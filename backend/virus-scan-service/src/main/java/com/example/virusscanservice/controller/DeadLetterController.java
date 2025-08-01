package com.example.virusscanservice.controller;

import com.example.virusscanservice.entity.DeadLetterMessage;
import com.example.virusscanservice.repository.DeadLetterMessageRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/deadletters")
public class DeadLetterController {

    private final DeadLetterMessageRepository repository;

    public DeadLetterController(DeadLetterMessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<DeadLetterMessage> all() {
        return repository.findAll();
    }
}

