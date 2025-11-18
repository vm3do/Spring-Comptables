package com.alaman.repository;

import com.alaman.entity.Document;
import com.alaman.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCompanyId(Long companyId);
    List<Document> findByStatus(DocumentStatus status);
}