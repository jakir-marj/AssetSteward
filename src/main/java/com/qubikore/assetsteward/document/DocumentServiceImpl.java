package com.qubikore.assetsteward.document;

import com.qubikore.assetsteward.asset.Asset;
import com.qubikore.assetsteward.asset.AssetRepository;
import com.qubikore.assetsteward.common.exception.ResourceNotFoundException;
import com.qubikore.assetsteward.common.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final AssetRepository assetRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    public DocumentServiceImpl(DocumentRepository documentRepository, AssetRepository assetRepository, FileStorageService fileStorageService) {
        this.documentRepository = documentRepository;
        this.assetRepository = assetRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public DocumentDTO createDocument(DocumentRequest documentRequest) {
        Document document = new Document();
        
        Asset asset = assetRepository.findById(documentRequest.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + documentRequest.getAssetId()));
        
        document.setAsset(asset);
        document.setDocumentType(documentRequest.getDocumentType());
        document.setExtractedText(documentRequest.getExtractedText());

        if (documentRequest.getFile() != null && !documentRequest.getFile().isEmpty()) {
            String fileUrl = fileStorageService.storeFile(documentRequest.getFile(), "documents");
            document.setFilePath(fileUrl);
        }

        Document savedDocument = documentRepository.save(document);
        return mapToDTO(savedDocument);
    }

    @Override
    public DocumentDTO getDocumentById(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        return mapToDTO(document);
    }

    @Override
    public List<DocumentDTO> getDocumentsByAssetId(UUID assetId) {
        return documentRepository.findByAssetAssetId(assetId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DocumentDTO updateDocument(UUID id, DocumentRequest documentRequest) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        if (documentRequest.getAssetId() != null) {
            Asset asset = assetRepository.findById(documentRequest.getAssetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + documentRequest.getAssetId()));
            document.setAsset(asset);
        }

        if (documentRequest.getDocumentType() != null && !documentRequest.getDocumentType().trim().isEmpty()) {
            document.setDocumentType(documentRequest.getDocumentType());
        }

        if (documentRequest.getExtractedText() != null) {
            document.setExtractedText(documentRequest.getExtractedText());
        }

        if (documentRequest.getFile() != null && !documentRequest.getFile().isEmpty()) {
            String fileUrl = fileStorageService.storeFile(documentRequest.getFile(), "documents");
            document.setFilePath(fileUrl);
        }

        Document updatedDocument = documentRepository.save(document);
        return mapToDTO(updatedDocument);
    }

    @Override
    @Transactional
    public void deleteDocument(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        documentRepository.delete(document);
    }

    private DocumentDTO mapToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setDocumentId(document.getDocumentId());
        
        if (document.getAsset() != null) {
            dto.setAssetId(document.getAsset().getAssetId());
        }
        
        dto.setDocumentType(document.getDocumentType());
        dto.setExtractedText(document.getExtractedText());
        dto.setUploadedAt(document.getUploadedAt());
        
        if (document.getFilePath() != null && !document.getFilePath().startsWith("http")) {
            dto.setFilePath(backendUrl + document.getFilePath());
        } else {
            dto.setFilePath(document.getFilePath());
        }
        
        return dto;
    }
}
