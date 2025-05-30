package com.inventory.service;

import com.inventory.model.Supplier;
import com.inventory.repository.InventoryManager;

import java.util.List;
import java.util.UUID;

/**
 * Service class for supplier management business logic.
 */
public class SupplierService {
    
    private final InventoryManager inventoryManager;
    
    public SupplierService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    
    public Supplier createSupplier(String companyName, String contactName, String email, String phone) {
        String supplierId = generateSupplierId();
        return new Supplier(supplierId, companyName, contactName, email, phone);
    }
    
    public boolean addSupplier(Supplier supplier) {
        return inventoryManager.addSupplier(supplier);
    }
    
    public Supplier getSupplierById(String supplierId) {
        return inventoryManager.getSupplierById(supplierId);
    }
    
    public List<Supplier> getAllSuppliers() {
        return inventoryManager.getAllSuppliers();
    }
    
    public boolean updateSupplier(Supplier supplier) {
        return inventoryManager.updateSupplier(supplier);
    }
    
    private String generateSupplierId() {
        return "SUP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 