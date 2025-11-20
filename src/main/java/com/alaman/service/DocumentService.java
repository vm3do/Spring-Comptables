package com.alaman.service;

import com.alaman.dto.DocumentRequestDTO;
import com.alaman.dto.DocumentResponseDTO;
import com.alaman.dto.DocumentUpdateDTO;
import com.alaman.entity.Document;
import com.alaman.entity.DocumentStatus;
import com.alaman.entity.User;
import com.alaman.mapper.DocumentMapper;
import com.alaman.repository.DocumentRepository;
import com.alaman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final DocumentMapper documentMapper;

    @Transactional
    public DocumentResponseDTO uploadDocument(DocumentRequestDTO requestDTO) {
        User currentUser = getCurrentUser();
        if (currentUser.getCompany() == null) {
            throw new RuntimeException("Only company users can upload documents.");
        }

        String filename = storageService.store(requestDTO.getFile());

        Document document = documentMapper.toEntity(requestDTO);
        document.setFilePath(filename);
        document.setCompany(currentUser.getCompany());
        document.setStatus(DocumentStatus.PENDING);

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toDTO(savedDocument);
    }

    public List<DocumentResponseDTO> getMyDocuments() {
        User currentUser = getCurrentUser();
        if (currentUser.getCompany() == null) {
             throw new RuntimeException("This method is for companies.");
        }
        return documentRepository.findByCompanyId(currentUser.getCompany().getId()).stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentResponseDTO> getAllDocuments() {
        // For Accountant
        return documentRepository.findAll().stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<DocumentResponseDTO> getPendingDocuments() {
        return documentRepository.findByStatus(DocumentStatus.PENDING).stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DocumentResponseDTO updateStatus(Long id, DocumentUpdateDTO updateDTO) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (updateDTO.getStatus() == DocumentStatus.REJECTED && (updateDTO.getAccountantComment() == null || updateDTO.getAccountantComment().isBlank())) {
            throw new RuntimeException("Justification is mandatory for rejection.");
        }

        document.setStatus(updateDTO.getStatus());
        document.setAccountantComment(updateDTO.getAccountantComment());
        document.setValidationDate(LocalDateTime.now());

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toDTO(savedDocument);
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}