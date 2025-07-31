package com.example.documentmanagementservice.repository;

import com.example.documentmanagementservice.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, String> {
}
