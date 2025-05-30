package com.inventory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Order item entity representing individual products within an order.
 * Links products to orders with quantity and pricing information.
 */
public class OrderItem {
    
    @JsonProperty("productId")
    private String productId;
    
    @JsonProperty("productName")
    private String productName;
    
    @JsonProperty("quantity")
    private int quantity;
    
    @JsonProperty("unitPrice")
    private double unitPrice;
    
    @JsonProperty("discount")
    private double discount;
    
    @JsonProperty("taxable")
    private boolean taxable;
    
    // Default constructor
    public OrderItem() {
        this.taxable = true;
    }
    
    // Constructor with essential fields
    public OrderItem(String productId, String productName, int quantity, double unitPrice) {
        this();
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Constructor from Product
    public OrderItem(Product product, int quantity) {
        this(product.getProductId(), product.getName(), quantity, product.getPrice());
        this.taxable = product.isTaxable();
    }
    
    // Getters and Setters
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public boolean isTaxable() {
        return taxable;
    }
    
    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }
    
    // Business logic methods
    public double getTotalPrice() {
        return (unitPrice * quantity) - discount;
    }
    
    public double getSubtotal() {
        return unitPrice * quantity;
    }
    
    public double getDiscountAmount() {
        return discount;
    }
    
    public double getNetPrice() {
        return unitPrice - (discount / quantity);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(productId, orderItem.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
    
    @Override
    public String toString() {
        return String.format("OrderItem{productId='%s', name='%s', quantity=%d, unitPrice=%.2f, total=%.2f}", 
                productId, productName, quantity, unitPrice, getTotalPrice());
    }
} 