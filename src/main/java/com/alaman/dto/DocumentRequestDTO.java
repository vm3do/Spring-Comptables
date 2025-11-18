package com.alaman.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DocumentRequestDTO {
    @NotBlank(message = "Document number is required")
    private String documentNumber;

    private String type;
    private String category;
    private LocalDate date;
    private BigDecimal amount;
    private String supplier;

    @NotNull(message = "File is required")
    private MultipartFile file;
}