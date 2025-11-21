package com.alaman.controller;

import com.alaman.dto.DocumentRequestDTO;
import com.alaman.dto.DocumentResponseDTO;
import com.alaman.dto.DocumentUpdateDTO;
import com.alaman.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<DocumentResponseDTO> uploadDocument(@Valid @ModelAttribute DocumentRequestDTO requestDTO) {
        return ResponseEntity.ok(documentService.uploadDocument(requestDTO));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<DocumentResponseDTO>> getMyDocuments() {
        return ResponseEntity.ok(documentService.getMyDocuments());
    }

    @GetMapping
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<DocumentResponseDTO> updateStatus(@PathVariable Long id, @RequestBody @Valid DocumentUpdateDTO updateDTO) {
        return ResponseEntity.ok(documentService.updateStatus(id, updateDTO));
    }
}