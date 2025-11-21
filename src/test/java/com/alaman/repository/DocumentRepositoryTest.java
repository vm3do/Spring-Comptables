package com.alaman.repository;

import com.alaman.entity.Company;
import com.alaman.entity.Document;
import com.alaman.entity.DocumentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    private Company testCompany1;
    private Company testCompany2;

    @BeforeEach
    void setUp() {
        testCompany1 = Company.builder()
                .name("Company One")
                .ice("ICE111111")
                .build();
        testCompany2 = Company.builder()
                .name("Company Two")
                .ice("ICE222222")
                .build();
        entityManager.persist(testCompany1);
        entityManager.persist(testCompany2);
        entityManager.flush();
    }

    @Test
    void shouldSaveDocument_WhenValidDataProvided() {
        // Arrange
        Document document = Document.builder()
                .documentNumber("DOC-001")
                .type("Invoice")
                .category("Sales")
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(1000.00))
                .supplier("Test Supplier")
                .filePath("/uploads/doc-001.pdf")
                .status(DocumentStatus.PENDING)
                .company(testCompany1)
                .build();

        // Act
        Document savedDocument = documentRepository.save(document);

        // Assert
        assertThat(savedDocument).isNotNull();
        assertThat(savedDocument.getId()).isNotNull();
        assertThat(savedDocument.getDocumentNumber()).isEqualTo("DOC-001");
        assertThat(savedDocument.getStatus()).isEqualTo(DocumentStatus.PENDING);
        assertThat(savedDocument.getCompany().getId()).isEqualTo(testCompany1.getId());
    }

    @Test
    void shouldFindDocumentsByCompanyId_WhenDocumentsExist() {
        // Arrange
        Document doc1 = Document.builder()
                .documentNumber("DOC-001")
                .type("Invoice")
                .filePath("/uploads/doc-001.pdf")
                .company(testCompany1)
                .build();
        Document doc2 = Document.builder()
                .documentNumber("DOC-002")
                .type("Receipt")
                .filePath("/uploads/doc-002.pdf")
                .company(testCompany1)
                .build();
        Document doc3 = Document.builder()
                .documentNumber("DOC-003")
                .type("Invoice")
                .filePath("/uploads/doc-003.pdf")
                .company(testCompany2)
                .build();
        entityManager.persist(doc1);
        entityManager.persist(doc2);
        entityManager.persist(doc3);
        entityManager.flush();

        // Act
        List<Document> documentsForCompany1 = documentRepository.findByCompanyId(testCompany1.getId());

        // Assert
        assertThat(documentsForCompany1).hasSize(2);
        assertThat(documentsForCompany1).extracting(Document::getDocumentNumber)
                .containsExactlyInAnyOrder("DOC-001", "DOC-002");
        assertThat(documentsForCompany1).allMatch(doc -> doc.getCompany().getId().equals(testCompany1.getId()));
    }

    @Test
    void shouldFindDocumentsByStatus_WhenDocumentsExist() {
        // Arrange
        Document pendingDoc = Document.builder()
                .documentNumber("DOC-001")
                .type("Invoice")
                .filePath("/uploads/doc-001.pdf")
                .status(DocumentStatus.PENDING)
                .company(testCompany1)
                .build();
        Document approvedDoc = Document.builder()
                .documentNumber("DOC-002")
                .type("Receipt")
                .filePath("/uploads/doc-002.pdf")
                .status(DocumentStatus.APPROVED)
                .company(testCompany1)
                .build();
        entityManager.persist(pendingDoc);
        entityManager.persist(approvedDoc);
        entityManager.flush();

        // Act
        List<Document> pendingDocuments = documentRepository.findByStatus(DocumentStatus.PENDING);

        // Assert
        assertThat(pendingDocuments).hasSize(1);
        assertThat(pendingDocuments.get(0).getDocumentNumber()).isEqualTo("DOC-001");
    }
}