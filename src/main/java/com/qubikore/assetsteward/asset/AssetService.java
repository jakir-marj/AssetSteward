package com.qubikore.assetsteward.asset;

import java.util.List;
import java.util.UUID;

public interface AssetService {
    AssetDTO createAsset(AssetDTO assetDTO);
    AssetDTO getAssetById(UUID id);
    List<AssetDTO> getAllAssets();
    AssetDTO updateAsset(UUID id, AssetDTO assetDTO);
    void deleteAsset(UUID id);
}
