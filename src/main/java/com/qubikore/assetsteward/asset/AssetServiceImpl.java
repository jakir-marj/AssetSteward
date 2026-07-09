package com.qubikore.assetsteward.asset;

import com.qubikore.assetsteward.category.Category;
import com.qubikore.assetsteward.category.CategoryRepository;
import com.qubikore.assetsteward.common.exception.ResourceNotFoundException;
import com.qubikore.assetsteward.user.User;
import com.qubikore.assetsteward.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final com.qubikore.assetsteward.document.DocumentRepository documentRepository;
    private final com.qubikore.assetsteward.maintenancelog.MaintenanceLogRepository maintenanceLogRepository;
    
    @org.springframework.beans.factory.annotation.Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    public AssetServiceImpl(AssetRepository assetRepository, 
                            UserRepository userRepository, 
                            CategoryRepository categoryRepository,
                            com.qubikore.assetsteward.document.DocumentRepository documentRepository,
                            com.qubikore.assetsteward.maintenancelog.MaintenanceLogRepository maintenanceLogRepository) {
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.documentRepository = documentRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
    }

    @Override
    @Transactional
    public AssetDTO createAsset(AssetDTO assetDTO) {
        Asset asset = new Asset();
        
        mapToEntity(assetDTO, asset);
        
        Asset savedAsset = assetRepository.save(asset);
        return mapToDTO(savedAsset);
    }

    @Override
    public AssetDTO getAssetById(UUID id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        return mapToDTO(asset);
    }

    @Override
    public List<AssetDTO> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssetDTO updateAsset(UUID id, AssetDTO assetDTO) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        
        mapToEntity(assetDTO, asset);
        
        Asset updatedAsset = assetRepository.save(asset);
        return mapToDTO(updatedAsset);
    }

    @Override
    @Transactional
    public void deleteAsset(UUID id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        assetRepository.delete(asset);
    }

    private void mapToEntity(AssetDTO assetDTO, Asset asset) {
        asset.setItemName(assetDTO.getItemName());
        asset.setBrand(assetDTO.getBrand());
        asset.setModelNumber(assetDTO.getModelNumber());
        asset.setSerialNumber(assetDTO.getSerialNumber());
        asset.setPurchasePrice(assetDTO.getPurchasePrice());
        asset.setPurchaseDate(assetDTO.getPurchaseDate());
        asset.setWarrantyMonths(assetDTO.getWarrantyMonths());
        asset.setWarrantyExpiryDate(assetDTO.getWarrantyExpiryDate());
        asset.setCurrentEstimatedValue(assetDTO.getCurrentEstimatedValue());
        asset.setLocation(assetDTO.getLocation());
        
        if (assetDTO.getUserId() != null) {
            User user = userRepository.findById(assetDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + assetDTO.getUserId()));
            asset.setUser(user);
        } else {
            asset.setUser(null);
        }
        
        if (assetDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(assetDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + assetDTO.getCategoryId()));
            asset.setCategory(category);
        } else {
            asset.setCategory(null);
        }
    }

    private AssetDTO mapToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setAssetId(asset.getAssetId());
        
        if (asset.getUser() != null) {
            dto.setUserId(asset.getUser().getId());
            dto.setUser(new com.qubikore.assetsteward.user.dto.ProfileResponse(asset.getUser(), backendUrl));
        }
        
        if (asset.getCategory() != null) {
            dto.setCategoryId(asset.getCategory().getCategoryId());
            com.qubikore.assetsteward.category.CategoryDTO categoryDTO = new com.qubikore.assetsteward.category.CategoryDTO();
            categoryDTO.setCategoryId(asset.getCategory().getCategoryId());
            categoryDTO.setName(asset.getCategory().getName());
            categoryDTO.setDescription(asset.getCategory().getDescription());
            if (asset.getCategory().getIconName() != null && !asset.getCategory().getIconName().startsWith("http")) {
                categoryDTO.setIconName(backendUrl + asset.getCategory().getIconName());
            } else {
                categoryDTO.setIconName(asset.getCategory().getIconName());
            }
            dto.setCategory(categoryDTO);
        }
        
        dto.setItemName(asset.getItemName());
        dto.setBrand(asset.getBrand());
        dto.setModelNumber(asset.getModelNumber());
        dto.setSerialNumber(asset.getSerialNumber());
        dto.setPurchasePrice(asset.getPurchasePrice());
        dto.setPurchaseDate(asset.getPurchaseDate());
        dto.setWarrantyMonths(asset.getWarrantyMonths());
        dto.setWarrantyExpiryDate(asset.getWarrantyExpiryDate());
        dto.setCurrentEstimatedValue(asset.getCurrentEstimatedValue());
        dto.setLocation(asset.getLocation());
        dto.setCreatedAt(asset.getCreatedAt());
        
        // Fetch and map documents
        if (asset.getAssetId() != null) {
            List<com.qubikore.assetsteward.document.Document> docs = documentRepository.findByAssetAssetId(asset.getAssetId());
            if (docs != null) {
                dto.setDocuments(docs.stream().map(d -> {
                    com.qubikore.assetsteward.document.DocumentDTO dDto = new com.qubikore.assetsteward.document.DocumentDTO();
                    dDto.setDocumentId(d.getDocumentId());
                    dDto.setAssetId(d.getAsset().getAssetId());
                    dDto.setDocumentType(d.getDocumentType());
                    if (d.getFilePath() != null && !d.getFilePath().startsWith("http")) {
                        dDto.setFilePath(backendUrl + d.getFilePath());
                    } else {
                        dDto.setFilePath(d.getFilePath());
                    }
                    dDto.setExtractedText(d.getExtractedText());
                    dDto.setUploadedAt(d.getUploadedAt());
                    return dDto;
                }).collect(Collectors.toList()));
            }
            
            // Fetch and map maintenance logs
            List<com.qubikore.assetsteward.maintenancelog.MaintenanceLog> logs = maintenanceLogRepository.findByAssetAssetId(asset.getAssetId());
            if (logs != null) {
                dto.setMaintenanceLogs(logs.stream().map(l -> {
                    com.qubikore.assetsteward.maintenancelog.MaintenanceLogDTO lDto = new com.qubikore.assetsteward.maintenancelog.MaintenanceLogDTO();
                    lDto.setLogId(l.getLogId());
                    lDto.setAssetId(l.getAsset().getAssetId());
                    lDto.setServiceDate(l.getServiceDate());
                    lDto.setDescription(l.getDescription());
                    lDto.setCost(l.getCost());
                    lDto.setPerformedBy(l.getPerformedBy());
                    lDto.setRecurring(l.getRecurring());
                    lDto.setNextDueDate(l.getNextDueDate());
                    lDto.setCreatedAt(l.getCreatedAt());
                    return lDto;
                }).collect(Collectors.toList()));
            }
        }
        
        return dto;
    }
}
