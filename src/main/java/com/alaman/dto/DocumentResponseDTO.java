package com.alaman.dto;

import com.alaman.entity.DocumentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DocumentResponseDTO {
    private Long id;
    private String documentNumber;
    private String type;
    private String category;
    private LocalDate date;
    private BigDecimal amount;
    private String supplier;
    private String filePath;
    private DocumentStatus status;
    private LocalDateTime validationDate;
    private String accountantComment;
    private String companyName;
    private LocalDateTime createdAt;
}