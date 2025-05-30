package com.inventory.util;

import com.inventory.model.ProductCategory;

/**
 * Utility class for advanced search and filtering operations.
 * Supports multiple criteria for efficient product searches.
 */
public class SearchFilter {
    
    private String productName;
    private ProductCategory category;
    private String supplierId;
    private Double minPrice;
    private Double maxPrice;
    private boolean lowStockOnly;
    private boolean activeOnly;
    private String barcode;
    private String sku;
    
    public SearchFilter() {
        this.activeOnly = true; // Default to show only active products
    }
    
    // Getters and Setters
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public ProductCategory getCategory() {
        return category;
    }
    
    public void setCategory(ProductCategory category) {
        this.category = category;
    }
    
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    
    public Double getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    
    public Double getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public boolean isLowStockOnly() {
        return lowStockOnly;
    }
    
    public void setLowStockOnly(boolean lowStockOnly) {
        this.lowStockOnly = lowStockOnly;
    }
    
    public boolean isActiveOnly() {
        return activeOnly;
    }
    
    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    // Builder pattern for easy filter construction
    public static class Builder {
        private SearchFilter filter = new SearchFilter();
        
        public Builder productName(String productName) {
            filter.setProductName(productName);
            return this;
        }
        
        public Builder category(ProductCategory category) {
            filter.setCategory(category);
            return this;
        }
        
        public Builder supplierId(String supplierId) {
            filter.setSupplierId(supplierId);
            return this;
        }
        
        public Builder priceRange(Double minPrice, Double maxPrice) {
            filter.setMinPrice(minPrice);
            filter.setMaxPrice(maxPrice);
            return this;
        }
        
        public Builder lowStockOnly(boolean lowStockOnly) {
            filter.setLowStockOnly(lowStockOnly);
            return this;
        }
        
        public Builder activeOnly(boolean activeOnly) {
            filter.setActiveOnly(activeOnly);
            return this;
        }
        
        public Builder barcode(String barcode) {
            filter.setBarcode(barcode);
            return this;
        }
        
        public Builder sku(String sku) {
            filter.setSku(sku);
            return this;
        }
        
        public SearchFilter build() {
            return filter;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Clear all filter criteria
     */
    public void clear() {
        this.productName = null;
        this.category = null;
        this.supplierId = null;
        this.minPrice = null;
        this.maxPrice = null;
        this.lowStockOnly = false;
        this.activeOnly = true;
        this.barcode = null;
        this.sku = null;
    }
    
    /**
     * Check if any filter criteria is set
     */
    public boolean hasFilters() {
        return productName != null || category != null || supplierId != null ||
               minPrice != null || maxPrice != null || lowStockOnly ||
               !activeOnly || barcode != null || sku != null;
    }
    
    @Override
    public String toString() {
        return String.format("SearchFilter{name='%s', category=%s, supplier='%s', priceRange=[%.2f-%.2f], lowStock=%b, active=%b}", 
                productName, category, supplierId, minPrice, maxPrice, lowStockOnly, activeOnly);
    }
} 