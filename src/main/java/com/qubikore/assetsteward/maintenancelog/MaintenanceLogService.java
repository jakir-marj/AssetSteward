package com.qubikore.assetsteward.maintenancelog;

import java.util.List;
import java.util.UUID;

public interface MaintenanceLogService {
    MaintenanceLogDTO createLog(MaintenanceLogDTO dto);
    MaintenanceLogDTO getLogById(UUID id);
    List<MaintenanceLogDTO> getLogsByAssetId(UUID assetId);
    MaintenanceLogDTO updateLog(UUID id, MaintenanceLogDTO dto);
    void deleteLog(UUID id);
}
