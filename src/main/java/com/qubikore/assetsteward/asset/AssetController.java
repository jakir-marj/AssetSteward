package com.qubikore.assetsteward.asset;

import com.qubikore.assetsteward.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AssetDTO>> createAsset(@Valid @RequestBody AssetDTO assetDTO) {
        AssetDTO createdAsset = assetService.createAsset(assetDTO);
        return new ResponseEntity<>(ApiResponse.success("Asset created successfully", createdAsset), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetDTO>> getAssetById(@PathVariable UUID id) {
        AssetDTO asset = assetService.getAssetById(id);
        return ResponseEntity.ok(ApiResponse.success("Asset retrieved successfully", asset));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssetDTO>>> getAllAssets() {
        List<AssetDTO> assets = assetService.getAllAssets();
        return ResponseEntity.ok(ApiResponse.success("Assets retrieved successfully", assets));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetDTO>> updateAsset(@PathVariable UUID id, @RequestBody AssetDTO assetDTO) {
        AssetDTO updatedAsset = assetService.updateAsset(id, assetDTO);
        return ResponseEntity.ok(ApiResponse.success("Asset updated successfully", updatedAsset));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(@PathVariable UUID id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok(ApiResponse.success("Asset deleted successfully", null));
    }
}
