package com.qubikore.assetsteward.asset;

import com.qubikore.assetsteward.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    @Transactional
    public AssetDTO createAsset(AssetDTO assetDTO) {
        Asset asset = new Asset();
        asset.setName(assetDTO.getName());
        asset.setDescription(assetDTO.getDescription());
        
        Asset savedAsset = assetRepository.save(asset);
        return mapToDTO(savedAsset);
    }

    @Override
    public AssetDTO getAssetById(Long id) {
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
    public AssetDTO updateAsset(Long id, AssetDTO assetDTO) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        
        asset.setName(assetDTO.getName());
        asset.setDescription(assetDTO.getDescription());
        
        Asset updatedAsset = assetRepository.save(asset);
        return mapToDTO(updatedAsset);
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        assetRepository.delete(asset);
    }
    
    private AssetDTO mapToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setDescription(asset.getDescription());
        return dto;
    }
}
