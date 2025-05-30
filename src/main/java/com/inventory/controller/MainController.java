package com.inventory.controller;

import com.inventory.InventoryApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Main controller for the primary application interface.
 */
public class MainController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label statsLabel;
    
    public void initialize() {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome to Inventory Management System");
        }
        
        if (statsLabel != null) {
            updateStats();
        }
    }
    
    private void updateStats() {
        if (InventoryApp.getInventoryManager() != null) {
            int productCount = InventoryApp.getInventoryManager().getProductCount();
            int orderCount = InventoryApp.getInventoryManager().getOrderCount();
            int supplierCount = InventoryApp.getInventoryManager().getSupplierCount();
            
            statsLabel.setText(String.format("Products: %d | Orders: %d | Suppliers: %d", 
                    productCount, orderCount, supplierCount));
        }
    }
} 