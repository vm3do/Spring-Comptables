package com.alaman.mapper;

import com.alaman.dto.DocumentRequestDTO;
import com.alaman.dto.DocumentResponseDTO;
import com.alaman.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "validationDate", ignore = true)
    @Mapping(target = "accountantComment", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Document toEntity(DocumentRequestDTO dto);

    @Mapping(target = "companyName", source = "company.name")
    DocumentResponseDTO toDTO(Document document);
}