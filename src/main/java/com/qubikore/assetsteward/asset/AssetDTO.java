package com.qubikore.assetsteward.asset;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class AssetDTO {

    private UUID assetId;
    private Long userId; // Keeping for backward compatibility or simple reference
    private Long categoryId; // Keeping for backward compatibility
    
    private com.qubikore.assetsteward.user.dto.ProfileResponse user;
    private com.qubikore.assetsteward.category.CategoryDTO category;
    
    private java.util.List<com.qubikore.assetsteward.document.DocumentDTO> documents;
    private java.util.List<com.qubikore.assetsteward.maintenancelog.MaintenanceLogDTO> maintenanceLogs;

    @NotBlank(message = "Item name is required")
    private String itemName;

    private String brand;
    private String modelNumber;
    private String serialNumber;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private Integer warrantyMonths;
    private LocalDate warrantyExpiryDate;
    private BigDecimal currentEstimatedValue;
    private String location;
    private LocalDateTime createdAt;

    // Getters and Setters

    public UUID getAssetId() {
        return assetId;
    }

    public void setAssetId(UUID assetId) {
        this.assetId = assetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public LocalDate getWarrantyExpiryDate() {
        return warrantyExpiryDate;
    }

    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
        this.warrantyExpiryDate = warrantyExpiryDate;
    }

    public BigDecimal getCurrentEstimatedValue() {
        return currentEstimatedValue;
    }

    public void setCurrentEstimatedValue(BigDecimal currentEstimatedValue) {
        this.currentEstimatedValue = currentEstimatedValue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public com.qubikore.assetsteward.user.dto.ProfileResponse getUser() {
        return user;
    }

    public void setUser(com.qubikore.assetsteward.user.dto.ProfileResponse user) {
        this.user = user;
    }

    public com.qubikore.assetsteward.category.CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(com.qubikore.assetsteward.category.CategoryDTO category) {
        this.category = category;
    }

    public java.util.List<com.qubikore.assetsteward.document.DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(java.util.List<com.qubikore.assetsteward.document.DocumentDTO> documents) {
        this.documents = documents;
    }

    public java.util.List<com.qubikore.assetsteward.maintenancelog.MaintenanceLogDTO> getMaintenanceLogs() {
        return maintenanceLogs;
    }

    public void setMaintenanceLogs(java.util.List<com.qubikore.assetsteward.maintenancelog.MaintenanceLogDTO> maintenanceLogs) {
        this.maintenanceLogs = maintenanceLogs;
    }
}
