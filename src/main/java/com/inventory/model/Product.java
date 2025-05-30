package com.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Product entity representing inventory items with comprehensive tracking capabilities.
 * Supports optimized data structures for efficient searching and sorting.
 */
public class Product {
    
    @JsonProperty("id")
    private String productId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("category")
    private ProductCategory category;
    
    @JsonProperty("price")
    private double price;
    
    @JsonProperty("quantity")
    private int quantity;
    
    @JsonProperty("reorderPoint")
    private int reorderPoint;
    
    @JsonProperty("supplierId")
    private String supplierId;
    
    @JsonProperty("barcode")
    private String barcode;
    
    @JsonProperty("sku")
    private String sku;
    
    @JsonProperty("weight")
    private double weight;
    
    @JsonProperty("dimensions")
    private String dimensions;
    
    @JsonProperty("imagePath")
    private String imagePath;
    
    @JsonProperty("lastUpdated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;
    
    @JsonProperty("active")
    private boolean active;
    
    @JsonProperty("taxable")
    private boolean taxable;
    
    @JsonProperty("cost")
    private double cost;
    
    // Default constructor for JSON deserialization
    public Product() {
        this.lastUpdated = LocalDateTime.now();
        this.active = true;
        this.taxable = true;
    }
    
    // Constructor with essential fields
    public Product(String productId, String name, ProductCategory category, double price, int quantity) {
        this();
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.reorderPoint = 10; // Default reorder point
    }
    
    // Getters and Setters
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        updateTimestamp();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        updateTimestamp();
    }
    
    public ProductCategory getCategory() {
        return category;
    }
    
    public void setCategory(ProductCategory category) {
        this.category = category;
        updateTimestamp();
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
        updateTimestamp();
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTimestamp();
    }
    
    public int getReorderPoint() {
        return reorderPoint;
    }
    
    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
        updateTimestamp();
    }
    
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
        updateTimestamp();
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    public void setBarcode(String barcode) {
        this.barcode = barcode;
        updateTimestamp();
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
        updateTimestamp();
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
        updateTimestamp();
    }
    
    public String getDimensions() {
        return dimensions;
    }
    
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
        updateTimestamp();
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        updateTimestamp();
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
        updateTimestamp();
    }
    
    public boolean isTaxable() {
        return taxable;
    }
    
    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
        updateTimestamp();
    }
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
        updateTimestamp();
    }
    
    // Business logic methods
    public boolean isLowStock() {
        return quantity <= reorderPoint;
    }
    
    public double getProfitMargin() {
        if (cost > 0) {
            return ((price - cost) / cost) * 100;
        }
        return 0;
    }
    
    public double getTotalValue() {
        return quantity * price;
    }
    
    public void adjustQuantity(int adjustment) {
        this.quantity += adjustment;
        updateTimestamp();
    }
    
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
    
    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', category=%s, price=%.2f, quantity=%d}", 
                productId, name, category, price, quantity);
    }
} 