package com.qubikore.assetsteward.document;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    DocumentDTO createDocument(DocumentRequest documentRequest);
    DocumentDTO getDocumentById(UUID id);
    List<DocumentDTO> getDocumentsByAssetId(UUID assetId);
    DocumentDTO updateDocument(UUID id, DocumentRequest documentRequest);
    void deleteDocument(UUID id);
}
