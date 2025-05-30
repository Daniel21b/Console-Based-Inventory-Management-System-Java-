package com.inventory.model;

/**
 * Enumeration of order statuses for tracking order lifecycle.
 */
public enum OrderStatus {
    PENDING("Pending", "Order received, awaiting processing"),
    PROCESSING("Processing", "Order is being prepared"),
    SHIPPED("Shipped", "Order has been shipped"),
    DELIVERED("Delivered", "Order has been delivered"),
    CANCELLED("Cancelled", "Order has been cancelled"),
    RETURNED("Returned", "Order has been returned"),
    REFUNDED("Refunded", "Order has been refunded");
    
    private final String displayName;
    private final String description;
    
    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 