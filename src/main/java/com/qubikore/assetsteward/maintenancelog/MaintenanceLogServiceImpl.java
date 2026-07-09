package com.qubikore.assetsteward.maintenancelog;

import com.qubikore.assetsteward.asset.Asset;
import com.qubikore.assetsteward.asset.AssetRepository;
import com.qubikore.assetsteward.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MaintenanceLogServiceImpl implements MaintenanceLogService {

    private final MaintenanceLogRepository logRepository;
    private final AssetRepository assetRepository;

    public MaintenanceLogServiceImpl(MaintenanceLogRepository logRepository, AssetRepository assetRepository) {
        this.logRepository = logRepository;
        this.assetRepository = assetRepository;
    }

    @Override
    @Transactional
    public MaintenanceLogDTO createLog(MaintenanceLogDTO dto) {
        MaintenanceLog log = new MaintenanceLog();
        mapToEntity(dto, log);
        MaintenanceLog savedLog = logRepository.save(log);
        return mapToDTO(savedLog);
    }

    @Override
    public MaintenanceLogDTO getLogById(UUID id) {
        MaintenanceLog log = logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance log not found with id: " + id));
        return mapToDTO(log);
    }

    @Override
    public List<MaintenanceLogDTO> getLogsByAssetId(UUID assetId) {
        return logRepository.findByAssetAssetId(assetId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MaintenanceLogDTO updateLog(UUID id, MaintenanceLogDTO dto) {
        MaintenanceLog log = logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance log not found with id: " + id));
        
        mapToEntity(dto, log);
        MaintenanceLog updatedLog = logRepository.save(log);
        return mapToDTO(updatedLog);
    }

    @Override
    @Transactional
    public void deleteLog(UUID id) {
        MaintenanceLog log = logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance log not found with id: " + id));
        logRepository.delete(log);
    }

    private void mapToEntity(MaintenanceLogDTO dto, MaintenanceLog log) {
        if (dto.getAssetId() != null) {
            Asset asset = assetRepository.findById(dto.getAssetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + dto.getAssetId()));
            log.setAsset(asset);
        }

        if (dto.getServiceDate() != null) {
            log.setServiceDate(dto.getServiceDate());
        }

        if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
            log.setDescription(dto.getDescription());
        }

        if (dto.getCost() != null) {
            log.setCost(dto.getCost());
        }

        if (dto.getPerformedBy() != null && !dto.getPerformedBy().trim().isEmpty()) {
            log.setPerformedBy(dto.getPerformedBy());
        }

        if (dto.getRecurring() != null) {
            log.setRecurring(dto.getRecurring());
        }

        if (dto.getNextDueDate() != null) {
            log.setNextDueDate(dto.getNextDueDate());
        }
    }

    private MaintenanceLogDTO mapToDTO(MaintenanceLog log) {
        MaintenanceLogDTO dto = new MaintenanceLogDTO();
        dto.setLogId(log.getLogId());
        
        if (log.getAsset() != null) {
            dto.setAssetId(log.getAsset().getAssetId());
        }
        
        dto.setServiceDate(log.getServiceDate());
        dto.setDescription(log.getDescription());
        dto.setCost(log.getCost());
        dto.setPerformedBy(log.getPerformedBy());
        dto.setRecurring(log.getRecurring());
        dto.setNextDueDate(log.getNextDueDate());
        dto.setCreatedAt(log.getCreatedAt());
        
        return dto;
    }
}
