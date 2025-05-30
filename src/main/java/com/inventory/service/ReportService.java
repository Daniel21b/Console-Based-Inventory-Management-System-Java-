package com.inventory.service;

import com.inventory.model.ProductCategory;
import com.inventory.repository.InventoryManager;

import java.util.Map;

/**
 * Service class for reporting and analytics business logic.
 */
public class ReportService {
    
    private final InventoryManager inventoryManager;
    
    public ReportService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    
    public double getTotalInventoryValue() {
        return inventoryManager.getTotalInventoryValue();
    }
    
    public double getTotalOrderValue() {
        return inventoryManager.getTotalOrderValue();
    }
    
    public Map<ProductCategory, Integer> getProductCountByCategory() {
        return inventoryManager.getProductCountByCategory();
    }
    
    public int getTotalProductCount() {
        return inventoryManager.getProductCount();
    }
    
    public int getTotalOrderCount() {
        return inventoryManager.getOrderCount();
    }
    
    public int getTotalSupplierCount() {
        return inventoryManager.getSupplierCount();
    }
} 