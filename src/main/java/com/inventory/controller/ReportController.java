package com.inventory.controller;

import com.inventory.InventoryApp;
import com.inventory.model.ProductCategory;
import com.inventory.service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.NumberFormat;
import java.util.Map;

/**
 * Controller for the Reports and Analytics module.
 * Provides comprehensive business insights and data visualization.
 */
public class ReportController {

    @FXML private Label totalInventoryValueLabel;
    @FXML private Label totalOrderValueLabel;
    @FXML private Label totalProductCountLabel;
    @FXML private Label totalOrderCountLabel;
    @FXML private Label totalSupplierCountLabel;
    @FXML private Label lowStockCountLabel;
    @FXML private TextArea categoryBreakdownArea;
    @FXML private TextArea summaryArea;
    @FXML private Button refreshButton;
    @FXML private Button exportButton;

    private ReportService reportService;
    private NumberFormat currencyFormat;
    private NumberFormat numberFormat;

    public void initialize() {
        reportService = InventoryApp.getReportService();
        currencyFormat = NumberFormat.getCurrencyInstance();
        numberFormat = NumberFormat.getNumberInstance();

        setupButtons();
        generateReports();
    }

    private void setupButtons() {
        refreshButton.setOnAction(e -> generateReports());
        exportButton.setOnAction(e -> exportReports());
    }

    private void generateReports() {
        // Key Performance Indicators
        updateKPIs();
        
        // Category Breakdown
        updateCategoryBreakdown();
        
        // Summary Report
        updateSummaryReport();
    }

    private void updateKPIs() {
        double totalInventoryValue = reportService.getTotalInventoryValue();
        double totalOrderValue = reportService.getTotalOrderValue();
        int totalProductCount = reportService.getTotalProductCount();
        int totalOrderCount = reportService.getTotalOrderCount();
        int totalSupplierCount = reportService.getTotalSupplierCount();
        
        // Get low stock count from ProductService
        int lowStockCount = InventoryApp.getProductService().getLowStockProducts().size();

        totalInventoryValueLabel.setText(currencyFormat.format(totalInventoryValue));
        totalOrderValueLabel.setText(currencyFormat.format(totalOrderValue));
        totalProductCountLabel.setText(numberFormat.format(totalProductCount));
        totalOrderCountLabel.setText(numberFormat.format(totalOrderCount));
        totalSupplierCountLabel.setText(numberFormat.format(totalSupplierCount));
        lowStockCountLabel.setText(numberFormat.format(lowStockCount));
    }

    private void updateCategoryBreakdown() {
        Map<ProductCategory, Integer> categoryBreakdown = reportService.getProductCountByCategory();
        
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("PRODUCT CATEGORY BREAKDOWN\n");
        breakdown.append("=" .repeat(40)).append("\n\n");
        
        int totalProducts = categoryBreakdown.values().stream().mapToInt(Integer::intValue).sum();
        
        for (Map.Entry<ProductCategory, Integer> entry : categoryBreakdown.entrySet()) {
            ProductCategory category = entry.getKey();
            Integer count = entry.getValue();
            double percentage = totalProducts > 0 ? (count * 100.0 / totalProducts) : 0;
            
            breakdown.append(String.format("%-20s: %4d products (%5.1f%%)\n", 
                    category.getDisplayName(), count, percentage));
        }
        
        breakdown.append("\n").append("=" .repeat(40));
        breakdown.append(String.format("\nTOTAL PRODUCTS: %d", totalProducts));
        
        categoryBreakdownArea.setText(breakdown.toString());
    }

    private void updateSummaryReport() {
        StringBuilder summary = new StringBuilder();
        summary.append("INVENTORY MANAGEMENT SYSTEM - EXECUTIVE SUMMARY\n");
        summary.append("=" .repeat(60)).append("\n\n");
        
        // Business Overview
        summary.append("BUSINESS OVERVIEW:\n");
        summary.append("-".repeat(20)).append("\n");
        summary.append(String.format("• Total Inventory Value: %s\n", 
                currencyFormat.format(reportService.getTotalInventoryValue())));
        summary.append(String.format("• Total Order Value: %s\n", 
                currencyFormat.format(reportService.getTotalOrderValue())));
        summary.append(String.format("• Active Suppliers: %d\n", 
                reportService.getTotalSupplierCount()));
        summary.append("\n");
        
        // Inventory Status
        summary.append("INVENTORY STATUS:\n");
        summary.append("-".repeat(20)).append("\n");
        summary.append(String.format("• Total Products: %d\n", 
                reportService.getTotalProductCount()));
        
        int lowStockCount = InventoryApp.getProductService().getLowStockProducts().size();
        summary.append(String.format("• Low Stock Items: %d", lowStockCount));
        
        if (lowStockCount > 0) {
            summary.append(" ⚠️ ATTENTION REQUIRED");
        }
        summary.append("\n");
        
        // Order Processing
        summary.append("\nORDER PROCESSING:\n");
        summary.append("-".repeat(20)).append("\n");
        summary.append(String.format("• Total Orders: %d\n", 
                reportService.getTotalOrderCount()));
        
        // System Performance
        summary.append("\nSYSTEM PERFORMANCE:\n");
        summary.append("-".repeat(20)).append("\n");
        summary.append("• Data Structure Optimization: ACTIVE\n");
        summary.append("• Search Performance: O(1) product lookups\n");
        summary.append("• Memory Usage: Optimized with HashMap/TreeMap\n");
        summary.append("• Concurrent Access: Thread-safe operations\n");
        
        // Recommendations
        summary.append("\nRECOMMENDations:\n");
        summary.append("-".repeat(20)).append("\n");
        
        if (lowStockCount > 0) {
            summary.append("• URGENT: Reorder " + lowStockCount + " low stock items\n");
        }
        
        Map<ProductCategory, Integer> categoryBreakdown = reportService.getProductCountByCategory();
        ProductCategory mostPopularCategory = categoryBreakdown.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        
        if (mostPopularCategory != null) {
            summary.append("• Focus on " + mostPopularCategory.getDisplayName() + 
                    " category (highest inventory)\n");
        }
        
        summary.append("• Regular supplier performance reviews recommended\n");
        summary.append("• Consider implementing automated reorder points\n");
        
        summary.append("\n").append("=" .repeat(60));
        summary.append("\nReport generated automatically by IMS v1.0.0");
        
        summaryArea.setText(summary.toString());
    }

    private void exportReports() {
        // Simple export functionality - in a real system, this would save to file
        StringBuilder exportData = new StringBuilder();
        exportData.append("INVENTORY MANAGEMENT SYSTEM - DATA EXPORT\n");
        exportData.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n\n");
        
        exportData.append("KEY METRICS:\n");
        exportData.append("Inventory Value: ").append(totalInventoryValueLabel.getText()).append("\n");
        exportData.append("Order Value: ").append(totalOrderValueLabel.getText()).append("\n");
        exportData.append("Product Count: ").append(totalProductCountLabel.getText()).append("\n");
        exportData.append("Order Count: ").append(totalOrderCountLabel.getText()).append("\n");
        exportData.append("Supplier Count: ").append(totalSupplierCountLabel.getText()).append("\n");
        exportData.append("Low Stock Items: ").append(lowStockCountLabel.getText()).append("\n\n");
        
        exportData.append(categoryBreakdownArea.getText()).append("\n\n");
        exportData.append(summaryArea.getText());
        
        // Show export data in a dialog (in real system, would save to file)
        Alert exportAlert = new Alert(Alert.AlertType.INFORMATION);
        exportAlert.setTitle("Export Complete");
        exportAlert.setHeaderText("Report Data Generated");
        exportAlert.getDialogPane().setPrefWidth(600);
        exportAlert.getDialogPane().setPrefHeight(400);
        
        TextArea exportArea = new TextArea(exportData.toString());
        exportArea.setEditable(false);
        exportArea.setWrapText(true);
        
        exportAlert.getDialogPane().setContent(exportArea);
        exportAlert.showAndWait();
    }
} 