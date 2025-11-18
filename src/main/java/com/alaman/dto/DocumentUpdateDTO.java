package com.alaman.dto;

import com.alaman.entity.DocumentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentUpdateDTO {
    @NotNull(message = "Status is required")
    private DocumentStatus status;

    private String accountantComment;
}