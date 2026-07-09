package com.qubikore.assetsteward.maintenancelog;

import com.qubikore.assetsteward.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/maintenance-logs")
public class MaintenanceLogController {

    private final MaintenanceLogService logService;

    public MaintenanceLogController(MaintenanceLogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MaintenanceLogDTO>> createLog(@Valid @RequestBody MaintenanceLogDTO dto) {
        MaintenanceLogDTO createdLog = logService.createLog(dto);
        return new ResponseEntity<>(ApiResponse.success("Maintenance log created successfully", createdLog), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceLogDTO>> getLogById(@PathVariable UUID id) {
        MaintenanceLogDTO log = logService.getLogById(id);
        return ResponseEntity.ok(ApiResponse.success("Maintenance log retrieved successfully", log));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<ApiResponse<List<MaintenanceLogDTO>>> getLogsByAssetId(@PathVariable UUID assetId) {
        List<MaintenanceLogDTO> logs = logService.getLogsByAssetId(assetId);
        return ResponseEntity.ok(ApiResponse.success("Maintenance logs retrieved successfully", logs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceLogDTO>> updateLog(@PathVariable UUID id, @RequestBody MaintenanceLogDTO dto) {
        MaintenanceLogDTO updatedLog = logService.updateLog(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Maintenance log updated successfully", updatedLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLog(@PathVariable UUID id) {
        logService.deleteLog(id);
        return ResponseEntity.ok(ApiResponse.success("Maintenance log deleted successfully", null));
    }
}
