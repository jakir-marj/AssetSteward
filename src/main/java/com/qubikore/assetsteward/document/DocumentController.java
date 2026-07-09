package com.qubikore.assetsteward.document;

import com.qubikore.assetsteward.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentDTO>> createDocument(@Valid @ModelAttribute DocumentRequest documentRequest) {
        DocumentDTO createdDocument = documentService.createDocument(documentRequest);
        return new ResponseEntity<>(ApiResponse.success("Document created successfully", createdDocument), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentDTO>> getDocumentById(@PathVariable UUID id) {
        DocumentDTO document = documentService.getDocumentById(id);
        return ResponseEntity.ok(ApiResponse.success("Document retrieved successfully", document));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<ApiResponse<List<DocumentDTO>>> getDocumentsByAssetId(@PathVariable UUID assetId) {
        List<DocumentDTO> documents = documentService.getDocumentsByAssetId(assetId);
        return ResponseEntity.ok(ApiResponse.success("Documents retrieved successfully", documents));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<ApiResponse<DocumentDTO>> updateDocument(@PathVariable UUID id, @ModelAttribute DocumentRequest documentRequest) {
        DocumentDTO updatedDocument = documentService.updateDocument(id, documentRequest);
        return ResponseEntity.ok(ApiResponse.success("Document updated successfully", updatedDocument));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }
}
